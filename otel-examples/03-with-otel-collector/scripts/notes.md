## Running traffic.sh in windows
### Points to note
1. Can be done because gitbash is installed
2. But requires installation **jq** on the machine and needs it to be accessible to gitbash<br\>
   This can be achieved with
   a) Start GitBash with admin privileges
   b) Run `curl -L -o /usr/bin/jq.exe https://github.com/stedolan/jq/releases/latest/download/jq-win64.exe`
   c) now run the shell script with <br\>
   		`cd \folder\with\script`
   		`.\traffic.sh`
