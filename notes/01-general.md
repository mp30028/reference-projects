
## Useful stuff
#### Get the value of an environment variable on windows
With powershell `echo $env:JAVA_HOME`<br/>
On command-prompt `echo %JAVA_HOME%`<br/>


## Issues
Unsolved<br/>
![Eclipse build warnings](./images/build-warnings.png)

Searched based on this log entry but without much success `WARNING: A Java agent has been loaded dynamically (....m2\repository\net\bytebuddy\byte-buddy-agent\1.14.13\byte-buddy-agent-1.14.13.jar)`<br/>
However noticed that if I switch of unit tests then the warning disappears.

