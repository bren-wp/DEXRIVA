#!/usr/bin/env python3
"""Render small, deterministic DEXRIVA window icons with Python stdlib only."""
import struct
import zlib
from pathlib import Path

OUT = Path(__file__).resolve().parents[1] / "jadx-gui/src/main/resources/logos"


def segment_distance(px, py, ax, ay, bx, by):
    dx, dy = bx - ax, by - ay
    t = max(0.0, min(1.0, ((px - ax) * dx + (py - ay) * dy) / (dx * dx + dy * dy)))
    return ((px - ax - t * dx) ** 2 + (py - ay - t * dy) ** 2) ** 0.5


def rgb(x, y):
    # Rounded navy tile, a stylised aqua > and subtle white underline.
    r = 0.16
    cx = max(r, min(1 - r, x))
    cy = max(r, min(1 - r, y))
    inside = (x - cx) ** 2 + (y - cy) ** 2 <= r * r
    if not inside:
        return (0, 0, 0, 0)
    color = (16, 25, 39, 255)
    if (
        segment_distance(x, y, .28, .28, .56, .50) < .065
        or segment_distance(x, y, .56, .50, .28, .72) < .065
    ):
        color = (52, 217, 208, 255)
    if segment_distance(x, y, .52, .72, .76, .72) < .034:
        color = (241, 245, 249, 255)
    return color


def chunk(kind, data):
    return struct.pack(">I", len(data)) + kind + data + struct.pack(">I", zlib.crc32(kind + data) & 0xFFFFFFFF)


def render(size):
    rows = bytearray()
    samples = 3
    for y in range(size):
        rows.append(0)
        for x in range(size):
            accum = [0, 0, 0, 0]
            for sy in range(samples):
                for sx in range(samples):
                    for i, val in enumerate(rgb((x + (sx + .5) / samples) / size, (y + (sy + .5) / samples) / size)):
                        accum[i] += val
            rows.extend(round(v / (samples * samples)) for v in accum)
    return (
        b"\x89PNG\r\n\x1a\n"
        + chunk(b"IHDR", struct.pack(">IIBBBBB", size, size, 8, 6, 0, 0, 0))
        + chunk(b"IDAT", zlib.compress(rows, level=9))
        + chunk(b"IEND", b"")
    )


if __name__ == "__main__":
    OUT.mkdir(parents=True, exist_ok=True)
    for size, name in [(16, "dexriva-logo-16px.png"), (32, "dexriva-logo-32px.png"), (48, "dexriva-logo-48px.png"), (256, "dexriva-logo.png")]:
        path = OUT / name
        path.write_bytes(render(size))
        print("Generated", path)
