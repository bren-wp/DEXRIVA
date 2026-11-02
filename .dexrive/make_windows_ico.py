#!/usr/bin/env python3
"""Build a multi-resolution Windows ICO with the deterministic DEXRIVE PNG renderer."""
import importlib.util
import struct
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
MODULE = ROOT / ".dexrive" / "make_icons.py"
spec = importlib.util.spec_from_file_location("dexrive_png", MODULE)
icons = importlib.util.module_from_spec(spec)
spec.loader.exec_module(icons)

sizes = (16, 24, 32, 48, 64, 128, 256)
images = [icons.render(size) for size in sizes]
offset = 6 + 16 * len(images)
directory = [struct.pack("<HHH", 0, 1, len(images))]
for size, data in zip(sizes, images):
    directory.append(struct.pack("<BBBBHHII", size if size < 256 else 0,
                                 size if size < 256 else 0, 0, 0, 1, 32, len(data), offset))
    offset += len(data)
dest = ROOT / "dexrive-gui" / "dist" / "windows" / "dexrive.ico"
dest.parent.mkdir(parents=True, exist_ok=True)
dest.write_bytes(b"".join(directory) + b"".join(images))
print(f"Created {dest} ({dest.stat().st_size} bytes)")
