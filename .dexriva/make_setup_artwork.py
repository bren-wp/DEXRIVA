#!/usr/bin/env python3
"""Generate original DEXRIVA NSIS wizard artwork using only Python stdlib.

The generated uncompressed BMPs are reproducible and contain no upstream logos.
"""
import struct
from pathlib import Path

BASE = Path(__file__).resolve().parents[1] / "packaging" / "windows" / "branding"
BASE.mkdir(parents=True, exist_ok=True)


def color_gradient(x, y, width, height):
    t = (x * 0.2 / width) + (y * 0.8 / height)
    base = tuple(round(a + (b - a) * t) for a, b in zip((23, 42, 63), (16, 25, 39)))
    return base


def dist(x, y, a, b, c, d):
    dx, dy = c - a, d - b
    q = max(0, min(1, ((x - a) * dx + (y - b) * dy) / (dx * dx + dy * dy)))
    return ((x - a - q * dx) ** 2 + (y - b - q * dy) ** 2) ** 0.5


def art(width, height, filename):
    stride = (width * 3 + 3) & ~3
    pixels = bytearray()
    for row in range(height - 1, -1, -1):
        for col in range(width):
            x = col / width
            y = row / height
            r, g, b = color_gradient(col, row, width, height)
            cy = 0.45 if height > 100 else 0.49
            cx = 0.50 if height > 100 else 0.53
            scale = 0.30 if height > 100 else 0.33
            dx = (x - cx) / scale
            dy = (y - cy) / scale
            if abs(dx) < 0.98 and abs(dy) < 0.76:
                r, g, b = (27, 57, 70)
            if (dist(dx, dy, -.62, -.52, .20, 0) < .09
                    or dist(dx, dy, .20, 0, -.62, .52) < .09):
                r, g, b = (52, 217, 208)
            if dist(dx, dy, .06, .50, .66, .50) < .07:
                r, g, b = (241, 245, 249)
            if col < 4:
                r, g, b = (52, 217, 208)
            pixels.extend((b, g, r))
        pixels.extend(bytes(stride - width * 3))
    header = b"BM" + struct.pack("<IHHI", 54 + len(pixels), 0, 0, 54)
    dib = struct.pack("<IiiHHIIiiII", 40, width, height, 1, 24, 0,
                      len(pixels), 2835, 2835, 0, 0)
    (BASE / filename).write_bytes(header + dib + pixels)
    print("Wrote", filename, len(pixels) + 54, "bytes")


if __name__ == "__main__":
    art(164, 314, "dexriva-welcome.bmp")
    art(150, 57, "dexriva-header.bmp")
