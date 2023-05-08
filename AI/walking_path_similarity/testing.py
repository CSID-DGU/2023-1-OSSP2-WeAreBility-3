from struct import *
import struct

# Example byte sequence
byte_seq = b"\xb6\x02\x02?\x00\x00\x80@"
a = struct.pack("f", 123.456)
print(a)
b = struct.unpack("f", a)
print(b)

"""# Use struct.unpack() to extract the value as a float
f = struct.unpack('<f', byte_seq)[0]

# Print the extracted value...
print(f)  # Output: 123.45600128173828"""
