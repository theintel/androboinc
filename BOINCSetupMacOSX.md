## 1. Install BOINC ##

_(Most probably the BOINC is already installed on your computer; In such case you can skip this chapter.)_

The BOINC Installer for Mac OS X will guide you through the installation. It's pretty straight-forward.

![http://androboinc.googlecode.com/svn/www/MacOSX_Installer.png](http://androboinc.googlecode.com/svn/www/MacOSX_Installer.png)

Right after installation you should see the following dialogue:

![http://androboinc.googlecode.com/svn/www/MacOSX_AfterInstall.png](http://androboinc.googlecode.com/svn/www/MacOSX_AfterInstall.png)

You should answer "Allow" here.

## 2. Protect BOINC client by password ##

If your BOINC is running, stop it.

By default, the BOINC data directory on Mac OS X is `/Library/Application Support/BOINC Data`. To set the password, open this directory in Finder:

![http://androboinc.googlecode.com/svn/www/MacOSX_BOINCDataDir.png](http://androboinc.googlecode.com/svn/www/MacOSX_BOINCDataDir.png)

You will see file `gui_rpc_auth.cfg` in the directory. Open the file in text editor and set your password here (overwrite the string which was generated at installation time).

## 3. Set BOINC to run as daemon ##

In BOINC Wiki page [Tools for Mac OS X](http://boinc.berkeley.edu/wiki/Tools_for_Mac_OS_X#Running_BOINC_as_a_daemon_or_system_service) you can find a script to set BOINC to run as a daemon (aka system service). Download the script `Make_BOINC_Service.sh` and follow the instructions (you can see the instructions by opening the script in text editor).

Do not restart the computer yet (as mentioned in last step of instructions), but move to next step.

## 4. Make BOINC client accessible via network ##

First, set permissions to service configuration, so you can edit them. In terminal type:
```
sudo chmod u+w /Library/LaunchDaemons/edu.berkeley.boinc.plist
```

Then edit the file:
```
sudo nano /Library/LaunchDaemons/edu.berkeley.boinc.plist
```

You must add parameter `--allow_remote_gui_rpc`, so the file will finally look like:
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
	<key>GroupName</key>
	<string>boinc_master</string>
	<key>Label</key>
	<string>edu.berkeley.boinc</string>
	<key>Program</key>
	<string>/Applications/BOINCManager.app/Contents/Resources/boinc</string>
	<key>ProgramArguments</key>
	<array>
		<string>/Applications/BOINCManager.app/Contents/Resources/boinc</string>
		<string>--redirectio</string>
		<string>--daemon</string>
		<string>--allow_remote_gui_rpc</string>
	</array>
	<key>RunAtLoad</key>
	<true/>
	<key>UserName</key>
	<string>boinc_master</string>
	<key>WorkingDirectory</key>
	<string>/Library/Application Support/BOINC Data/</string>
</dict>
</plist>
```

_Note: Most probably the parameters will have one dash only (`-redirectio` and `-daemon`). It seems to work fine, nevertheless all documentation and help texts printed by BOINC itself say about double dashes (`--redirectio` and `--daemon`) and this is also Unix-style of long options._

Finally, restart your computer after modification of the file.

## 5. Verify the system service and password ##

After restart, your BOINC should be running as a system service and should be protected by password. To verify system service, open the terminal and type:
```
sudo launchctl list edu.berkeley.boinc
```
You should see something like:
```
{
	"Label" = "edu.berkeley.boinc";
	"LimitLoadToSessionType" = "System";
	"OnDemand" = true;
	"LastExitStatus" = 0;
	"PID" = 432;
	"TimeOut" = 30;
	"Program" = "/Applications/BOINCManager.app/Contents/Resources/boinc";
	"ProgramArguments" = (
		"/Applications/BOINCManager.app/Contents/Resources/boinc";
		"--redirectio";
		"--daemon";
		"--allow_remote_gui_rpc";
	);
};
```
This means that your BOINC is running as a service and accepts remote control from network.

You can also verify that your password works: In BOINC Manager switch to Advanced view, use menu `Advanced->Select computer...` and in the dialog use host name 127.0.0.1 and first try other password than the one you have chosen. With incorrect password you will get error message. After entering the correct password you will connect successfully and you can control your BOINC.

## 6. Firewall ##

Firewall is recommended for security, so it should always be on.

If you installed BOINC according to chapter 1, you already allowed access to BOINC in firewall. For the case you had BOINC installed previously, you may need to adjust the firewall settings.

Open the `System Preferences` in Apple menu, then choose `Security` icon. In the Firewall tab select `Advanced` button. Make sure that for `boinc` the incoming connections are allowed:

![http://androboinc.googlecode.com/svn/www/MacOSX_FirewallSettings.png](http://androboinc.googlecode.com/svn/www/MacOSX_FirewallSettings.png)

If there's no boinc entry in the list, you have to add it. Open /Applications in Finder. Find the BOINCManager.app there, press right mouse button and select `Show Package Contents`.

![http://androboinc.googlecode.com/svn/www/MacOSX_FirewallAdd.png](http://androboinc.googlecode.com/svn/www/MacOSX_FirewallAdd.png)

In the newly opened window go into Contents and then into Resources. You will see the boinc there.

![http://androboinc.googlecode.com/svn/www/MacOSX_BOINCClient.png](http://androboinc.googlecode.com/svn/www/MacOSX_BOINCClient.png)

Drag `boinc` to the list of firewall rules, so `boinc` will appear there and you can select `Allow incoming connections`.
