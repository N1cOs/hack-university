# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: helloworld.proto

import sys
_b=sys.version_info[0]<3 and (lambda x:x) or (lambda x:x.encode('latin1'))
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor.FileDescriptor(
  name='helloworld.proto',
  package='communicaiton',
  syntax='proto3',
  serialized_options=_b('\n\025ru.ifmo.se.protofilesB\022CommunicationProtoP\001\242\002\003HLW'),
  serialized_pb=_b('\n\x10helloworld.proto\x12\rcommunicaiton\"n\n\x08Musician\x12\x0c\n\x04name\x18\x01 \x01(\t\x12\x0f\n\x07generes\x18\x02 \x03(\t\x12\x0e\n\x06tracks\x18\x03 \x03(\t\x12\x11\n\ttimestamp\x18\x04 \x01(\t\x12\x0f\n\x07x_coord\x18\x05 \x01(\x01\x12\x0f\n\x07y_coord\x18\x06 \x01(\x01\"\x0e\n\x0c\x45mptyMessage2\x90\x01\n\x0c\x43ommunicator\x12@\n\x04Poll\x12\x1b.communicaiton.EmptyMessage\x1a\x17.communicaiton.Musician\"\x00\x30\x01\x12>\n\x04Send\x12\x17.communicaiton.Musician\x1a\x1b.communicaiton.EmptyMessage\"\x00\x42\x33\n\x15ru.ifmo.se.protofilesB\x12\x43ommunicationProtoP\x01\xa2\x02\x03HLWb\x06proto3')
)




_MUSICIAN = _descriptor.Descriptor(
  name='Musician',
  full_name='communicaiton.Musician',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='name', full_name='communicaiton.Musician.name', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='generes', full_name='communicaiton.Musician.generes', index=1,
      number=2, type=9, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='tracks', full_name='communicaiton.Musician.tracks', index=2,
      number=3, type=9, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='timestamp', full_name='communicaiton.Musician.timestamp', index=3,
      number=4, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='x_coord', full_name='communicaiton.Musician.x_coord', index=4,
      number=5, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='y_coord', full_name='communicaiton.Musician.y_coord', index=5,
      number=6, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=35,
  serialized_end=145,
)


_EMPTYMESSAGE = _descriptor.Descriptor(
  name='EmptyMessage',
  full_name='communicaiton.EmptyMessage',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=147,
  serialized_end=161,
)

DESCRIPTOR.message_types_by_name['Musician'] = _MUSICIAN
DESCRIPTOR.message_types_by_name['EmptyMessage'] = _EMPTYMESSAGE
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

Musician = _reflection.GeneratedProtocolMessageType('Musician', (_message.Message,), dict(
  DESCRIPTOR = _MUSICIAN,
  __module__ = 'helloworld_pb2'
  # @@protoc_insertion_point(class_scope:communicaiton.Musician)
  ))
_sym_db.RegisterMessage(Musician)

EmptyMessage = _reflection.GeneratedProtocolMessageType('EmptyMessage', (_message.Message,), dict(
  DESCRIPTOR = _EMPTYMESSAGE,
  __module__ = 'helloworld_pb2'
  # @@protoc_insertion_point(class_scope:communicaiton.EmptyMessage)
  ))
_sym_db.RegisterMessage(EmptyMessage)


DESCRIPTOR._options = None

_COMMUNICATOR = _descriptor.ServiceDescriptor(
  name='Communicator',
  full_name='communicaiton.Communicator',
  file=DESCRIPTOR,
  index=0,
  serialized_options=None,
  serialized_start=164,
  serialized_end=308,
  methods=[
  _descriptor.MethodDescriptor(
    name='Poll',
    full_name='communicaiton.Communicator.Poll',
    index=0,
    containing_service=None,
    input_type=_EMPTYMESSAGE,
    output_type=_MUSICIAN,
    serialized_options=None,
  ),
  _descriptor.MethodDescriptor(
    name='Send',
    full_name='communicaiton.Communicator.Send',
    index=1,
    containing_service=None,
    input_type=_MUSICIAN,
    output_type=_EMPTYMESSAGE,
    serialized_options=None,
  ),
])
_sym_db.RegisterServiceDescriptor(_COMMUNICATOR)

DESCRIPTOR.services_by_name['Communicator'] = _COMMUNICATOR

# @@protoc_insertion_point(module_scope)