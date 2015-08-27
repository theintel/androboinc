**Q: Could the feature XY be added to AndroBOINC?**

**A:** You are welcome to request for enhancement can be submitted via Issue tracker. The [issue 6](https://code.google.com/p/androboinc/issues/detail?id=6) or [issue 12](https://code.google.com/p/androboinc/issues/detail?id=12) are examples of such enhancement requests.

**Q: The AndroBOINC always force closes on my xxxxxx phone. Why?**

**A:** If it is older Android phone or just cheaper one, it is possible that phone does not have enough memory. The BOINC client can send quite a lot of data to AndroBOINC (depending also on number of projects, applications, tasks and messages). Although memory usage was considered in AndroBOINC design and memory is not wasted, there is some minimum usage and it is possible that phones with too little free emory just cannot handle it.

**Q: I can't get this to work. The networking setup is too complicated and needs to be simplified some.**

**A:** The setup of AndroBOINC itself is easy. The setup of BOINC client is more complex, but that is a feature of BOINC client itself (and cannot be changed by AndroBOINC). The procedure described [in this wiki](HowToSetup.md) now covers all 3 major PC platforms and should work for most of cases.
Some scripts could possibly make setup of BOINC client easier, but due to the nature of BOINC (multiple platforms, different options of installation, etc) there would be risk of breaking things (but everybody is welcome to provide working solution for this).
The setup related to networking (e.g. port forwarding, firewall setting) depends on environment and can vary a lot - no universal solution exists.

**Q: Can I compile AndroBOINC myself?**

**A:** Yes, you can! AndroBOINC is open source project, published under GPLv3. The description of exact compilation steps is not published in the wiki yet (it is planned), but if you are familiar with building for Android, you don't need it anyway.

**Q: Can we get also BOINC _CLIENT_ on Android?**

**A:** BOINC client is not part of AndroBOINC project. There are several reasons for that:
  1. _Battery._ Android devices are mainly mobile devices and running computation on them would consume battery extremely fast.
  1. _Processor architecture._ Android devices mostly run on ARM processors. So some adaptation of BOINC client code and compilation for ARM should be done first. But what should be computed then? It would need also project applications compiled for ARM...
  1. _Android system restrictions._ BOINC client downloads applications from project page and runs them - while this is simple on operating systems for PC, in Android the applications must be installed into special area by special procedure. Several functional changes of client would be necessary to be able to run scientific applications on Android system.

With the continuous development of Android platform the making of BOINC client could become more and more reasonable (e.g. for tablets with multicore CPUs) so it is possible that somebody will start another project for client someday, but for AndroBOINC it is currently not planned.