# Introduction #

This page gives description about basic steps needed to make AndroBOINC working.

User needs to:
  * Set [BOINC](http://en.wikipedia.org/wiki/BOINC) client so it can start up automatically
  * Protect BOINC client by password
  * Attach to BOINC projects from PC (this cannot be done from AndroBOINC yet)
  * Make BOINC client accessible by network
  * Install AndroBOINC on Android device
  * Set up AndroBOINC, so it can communicate with BOINC client(s)


# BOINC client configuration #

AndroBOINC is remote control for BOINC core client. The BOINC core client must be set up correctly first. This chapters shows basic step-by-step example, suitable for unexperienced users. Experienced BOINC users have probably customized their BOINC client already and need only update its configurations, which can also be done based on this example.


## BOINC setup ##

Instructions differ depending on platform which is used to run BOINC:

  * [BOINC setup on Linux](BOINCSetupLinux.md)

  * [BOINC setup on Mac OS X](BOINCSetupMacOSX.md)

  * [BOINC setup on Windows](BOINCSetupWindows.md)


# Accessibility from Internet #

Today it is quite common to have several network devices at home, while using one router to access Internet. Typically the router performs [network address translation (NAT)](http://en.wikipedia.org/wiki/Network_address_translation). In such case the devices on home network are not directly accessible from Internet, so PC running BOINC core client is also inaccessible from Internet.

In some cases user may wish to have BOINC client inaccessible from Internet and use AndroBOINC only on home [WiFi](http://en.wikipedia.org/wiki/WiFi) network.

But in most cases user wants to control BOINC remotely also from Internet.

To make BOINC accessible from Internet, port forwarding must be enabled on router, so when port 31416 is accessed from Internet, the connection is forwarded to the PC where the BOINC client is installed and listening on port 31416. As there are various routers with different ways how to set them up, it is not possible to describe details in this document.

After BOINC client is accessible from Internet, we must know its address to be able to connect to it. Static IP-address does not change; in this case external IP-address of router can be directly used to connect to BOINC client from Internet. For static addresses it is also easy to use normal [Domain Name System (DNS)](http://en.wikipedia.org/wiki/Domain_Name_System) and access client via domain name.

With dynamic addresses it is more difficult, because address changes over time. It is still possible to set up [dynamic DNS (DDNS)](http://en.wikipedia.org/wiki/DDNS) in this case.

In the worst case a small trick can be used, to find current dynamic IP-address of BOINC client: BOINC client contacts project servers periodically to get new work and report finished work. Project server stores the IP-address of a client and the address can be displayed via user's account on project's page. User must log on the project page, navigate to "Computers on this account", choose "Details" of the computer and on computer information page click on "Show IP address". The "External IP address" field shows the IP-address used by BOINC client in the latest contact, so if dynamic IP-address does not change too often, there is a good chance that this latest IP-address is still valid.


# AndroBOINC configuration #

Once the BOINC client is set up and accessible, here comes the easier part: setting the AndroBOINC.

  1. Install AndroBOINC on Android device.
  1. Start AndroBOINC.
  1. Press `Menu` button, then select `Connect` from menu.
  1. After the fresh installation the list is empty, but screen gives you hint. Press `Menu` button, then select `Add new host`.
  1. Fill the form:
    * `Host nickname` is the name which will appear in lists and titles. Any name can be used, but it must be unique.
    * `Address` is either IP-address or domain name of the BOINC client - client must be reachable via this address.
    * `Port` is the port number the BOINC client is using.
    * `Password` is... well... the password.
  1. After filling the first BOINC client, it appears in list. Another BOINC client can be added by pressing `Menu` button again and filling the address of the other host.
  1. Selecting the host from the list will initiate connection towards BOINC client.

After connect the BOINC client can be controlled by AndroBOINC. The list of BOINC clients is saved in AndroBOINC's database, so next time the AndroBOINC is invoked, the BOINC client can be selected directly. In case modification of previously configured host is needed (e.g. when password is changed), or if obsolete host should be removed from list, the long touch on list item will bring context menu for selected item and it can be modified or deleted then.
