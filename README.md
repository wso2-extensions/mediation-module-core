# Mediation Module  Core Library

This repository contains supporting functions for common features required in developing a Mediation module for
Integration Studio. The library can also be used by Class Mediator developers to work with different 
payload types. Developers have to extend their Class Mediator class with SimpleMediator.
Then in the mediate method, they can use SimpleMessageContext to use features provided by this library.

# Mediation Module Core Library Features

- Get payload from MessageContext (Json, Xml, Csv)
- Get payload as Java Stream
- Set payload to MessageContext
- User collectors to set payload when using Java Stream