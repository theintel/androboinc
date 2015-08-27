This example works for Debian or Ubuntu. The exact procedure may slightly differ for other Linux distributions.

## 1. Install BOINC client ##

First, the BOINC core client must be set up the right way. The easiest is to use packages from distribution.
Run following command from terminal:
```
sudo apt-get install boinc-client boinc-manager
```
_Hint: If the machine is headless server (no GUI needed on that machine), the package boinc-manager can be omitted. But another machine with BOINC manager is still needed in this case (to attach to project) and this kind of setup is not suitable for unexperienced users._

Now the BOINC client will start automatically when computer starts. The configuration file for BOINC client startup is `/etc/default/boinc-client`, client runs in directory `/var/lib/boinc-client` and various options for client are set in the files in directory `/etc/boinc-client`.

## 2. Protect BOINC client by password ##

Now set up password to protect your BOINC client, so only authorized managers can control it:
```
sudo gedit /etc/boinc-client/gui_rpc_auth.cfg
```
_Hint: You can also use your favorite editor instead of gedit._

Then write your chosen password (one line only) in the editor window, save file and exit editor.

**Note: Setting of password is important! Do not neglect this point. Make sure your BOINC client is secure before making it accessible from the network**

To apply the settings, we need to restart BOINC client:
```
sudo /etc/init.d/boinc-client restart
```

## 3. Atach to BOINC projects ##

Next we will use normal BOINC Manager to manage BOINC core client. We will actually achieve 2 things in this step:
  1. Attach to BOINC project(s)
  1. Verify that the password that we set up is working correctly

BOINC manager can be started from desktop menu. On Ubuntu it is located in `Applications->System Tools`. The manager can be also started from terminal window by typing:
```
boincmgr
```

After start, the BOINC manager can be automatically connected to the BOINC client on the same machine (usually the case when starting from menu and BOINC client uses standard port 31416). In such case attach to project is offered for newly installed BOINC client.

It is also possible, that manager cannot find the client right after starting. In such case switch to Advanced view of manager and then use menu `Advanced->Select computer...` and in the dialog use host name (or IP-address instead) and the password chosen. If BOINC client was configured to use non-standard port (see also below), the port can be also specified, for example: `127.0.0.1:31418`

In any case, connecting to client from standard manager is used to verify the password protection of the BOINC client.

## 4. Make BOINC client accessible via network ##

Now we tell to BOINC client to listen on network:
```
sudo gedit /etc/default/boinc-client
```

On the bottom of the file there is something like:
```
# Here you can specify additional options to pass to the BOINC core client.
# Type 'boinc --help' or 'man boinc' for a full summary of allowed options.
#BOINC_OPTS="--allow_remote_gui_rpc"
BOINC_OPTS=""
```

What we need is actually mentioned in comment right above BOINC\_OPTS setting (comments are starting with character `#`); We need `--allow_remote_gui_rpc`, so the end of the file will then look like:
```
# Here you can specify additional options to pass to the BOINC core client.
# Type 'boinc --help' or 'man boinc' for a full summary of allowed options.
#BOINC_OPTS="--allow_remote_gui_rpc"
BOINC_OPTS="--allow_remote_gui_rpc"
```

The BOINC core client listens by default on TCP port 31416. In case we need to use another port, we can specify it here as well, the end of file will be then:
```
# Here you can specify additional options to pass to the BOINC core client.
# Type 'boinc --help' or 'man boinc' for a full summary of allowed options.
#BOINC_OPTS="--allow_remote_gui_rpc"
BOINC_OPTS="--allow_remote_gui_rpc --gui_rpc_port 31417"
```

To apply the settings, we again need to restart BOINC client:
```
sudo /etc/init.d/boinc-client restart
```

### Firewall ###

The access to computer can be additionally guarded by firewall. This is recommended for security. There are many firewalls out there, so settings need to be adapted accordingly. Here is simple example for `ufw`:

Check the status of firewall:
```
sudo ufw status
```
Enable firewall (this blocks access to BOINC core client by default):
```
sudo ufw enable
```
Allow access to BOINC client from any network:
```
sudo ufw allow 31416/tcp
```
_Note: If you use --gui\_rpc\_port, adjust port number accordingly_
