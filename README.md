# routing-info-protocol

Implementation of RIP V2.0 (distance-vector routing protocol).
This applicaiton represents a router that can run RIP in active mode. It advertizes 
its routes to others, listens to RIP messages and uses them to update its routing table.
It uses UDP datagram socket for transmitting information.

Following functionalities are supported:

1) Active RIP at routers 
2) Handle incoming route messages
3) CIDR
4) Route message broadcasts
5) Recovery from failed links and router, if alternate routes are available 
6) Split horizon with poisoned reverse, to avoid count to infinity problem 


References:

[RIP Version 1](https://www.ietf.org/rfc/rfc1058.txt)

[RIP Version 2](https://tools.ietf.org/html/rfc2453)

Notes:
Route update time is set to 1 second, not 30 seconds as defined in the RFC, to reduce the convergence time.
