# CS471-65 Introduction to Software Engineering

ขอบคุณโปรเจคต้นแบบจากนิสิตรายวิชา 01418211-Software Construction เพื่อเป็นโค้ดตัวอย่างในการปรับปรุงโค้ดดียิ่งขึ้นด้วยหลักการต่าง ๆ เช่น SOLID, CLEAN etc.

<br>

<h1 align="center">CS211-611 Project HomoSapiens</h1>
<p align="center">
    <b>Project ภาคต้น 2566</b> <br>
    <i>An open-source,</i> Simple Event Management program.<br>
    <br>
    <br><img width="922" alt="project-banner" src="https://github.com/n-thnptp/readmetest/assets/96496274/b2f63b36-deb9-46f7-b186-7cc9dafd97f8"><br>
    <br>
    <br>
    <a href="#-about">❓ About</a>‎ ‎ |‎ ‎ 
    <a href="#-sample-user-data">📋 Sample Data</a>‎ ‎ |‎ ‎ 
    <a href="#%EF%B8%8F-installation">⚙️ Installation</a>‎ ‎ |‎ ‎ 
    <a href="#-files-structure">📁 Files Structure</a>‎ ‎ |‎ ‎  
    <b><a href="https://github.com/CS211-661/cs211-661-project-homo-sapien/blob/main/วิธีการใช้งานEventFiesta.pdf">📚 Read the manual</a></b><br>
</p>

## Project detail
https://saacsos.notion.site/Project-CS211-2566-1c8e195c0ebd4071aea8f733692e3989?pvs=4

---

## ❓ About
โปรแกรมนี้เป็นโปรเจคของกลุ่มนักศึกษาจากมหาวิทยาลัยเกษตรศาสตร์ เป็นส่วนหนึ่งของรายวิชา <b>CS211-611 (01418211-65)</b>

---

## 📋 Sample User Data
<pre>
#-----------admin account-----------#
admin : 1234
#------------user account------------#
b6510405610 : ku66
b6510405504 : ku66
b6510405580 : ku66
b6510405598 : ku66
j.smith : SecurePass!123
m.johnson : [email protected]$e&876
jessica.wilson : Pa$$w0rd#456
j.brown : [email protected]$tr0ng
sophia.lee : MyP@ssw0rd99
emma.hall : [email protected]$w0rd!
s.clark : MyP@ssw0rd#1
s.white : [email protected]!456
l.hall : S3cur3P@ssw0rd!
c.mitchell : P@ssw0rd4567
e.king : KingOfP@ssw0rd$
grace.walker : W@lk3rS3cur1ty!
emily.miller : StrongP@ss789
myers        : myers1234
miguel       : miguel2345
miles        : miles69
gwen         : transkid56
pavitr       : chainottea
bobby        : fascistmustdi3
peter        : parker3333
michellej    : im_not_redhead
dark_knight  : wayne1111
superman     : krypto00
f.lash       : toofast2type
akira22      : iloverugbies

</pre>

---

## ⚙️ Installation
### Requirements
- Windows 10 or Windows 11
- <a href="https://www.java.com/en/">Java</a> or <a href="https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html">JDK 17</a>

### Download
- Download the latest executable jar file from the <b><a href="https://github.com/CS211-661/cs211-661-project-homo-sapien/releases">RELEASE PAGE</a></b> or <b><a href="#cloning-the-project">CLONE THIS PROJECT</a></b>
- Run the program by double clicking the downloaded executable jar file

### Running the Program via CLI
Replace ${version} with your downloaded version:
```bash
java -jar cs211-661-project-${version}-jar-with-dependencies.jar
```
Example: 
```bash
java -jar cs211-661-project-0.3.0-jar-with-dependencies.jar
```

### Cloning the Project
Clone the project:
```bash
git clone https://github.com/CS211-661/cs211-661-project-homo-sapien.git
```

Move the current directory:
```bash
cd cs211-661-project-homo-sapien/example-build
```

Run the program:
```bash
java -jar event-fiesta-v0.4.0-jar-with-dependencies.jar
```

---

## 📁 Files Structure
<pre>
📦cs211-661-project-homo-sapien
┣ <a href="https://github.com/CS211-661/cs211-661-project-homo-sapien/tree/main/data">📂data</a>                               # stores user data in csv (eg. account data, events)
┃ ┣ <a href="https://github.com/CS211-661/cs211-661-project-homo-sapien/tree/main/data/poster_img">📂poster_img</a>                       # stores event poster images (*.png, *.jpg, *.jpeg)
┃ ┗ <a href="https://github.com/CS211-661/cs211-661-project-homo-sapien/tree/main/user_img">📂user_img</a>                         # stores user images (*.png, *.jpg, *.jpeg, *.gif)
┣ <a href="https://github.com/CS211-661/cs211-661-project-homo-sapien/tree/main/example-build">📂example-build</a>                      # latest build with example data
┣ <a href="https://github.com/CS211-661/cs211-661-project-homo-sapien/tree/main/releases">📂releases</a>                           # previous releases
┣ <a href="https://github.com/CS211-661/cs211-661-project-homo-sapien/tree/main/src/main">📂src/main</a>
┃ ┗ <a href="https://github.com/CS211-661/cs211-661-project-homo-sapien/tree/main/src/main/java">📂java</a>
┃ ┃ ┗ <a href="https://github.com/CS211-661/cs211-661-project-homo-sapien/tree/main/src/main/java/cs211/project">📂cs211/project</a>                  # stores models, controllers, services
┃ ┃ ┃ ┣ <a href="https://github.com/CS211-661/cs211-661-project-homo-sapien/tree/main/src/main/java/cs211/project/controllers">📂controllers</a>                  # handles request flow and render views
┃ ┃ ┃ ┣ <a href="https://github.com/CS211-661/cs211-661-project-homo-sapien/tree/main/src/main/java/cs211/project/cs211661project">📂cs211611project</a>              # responsible for launching the program & storing ui paths
┃ ┃ ┃ ┣ <a href="https://github.com/CS211-661/cs211-661-project-homo-sapien/tree/main/src/main/java/cs211/project/models">📂models</a>                       # handles data logic & interacts with data
┃ ┃ ┃ ┃ ┗ <a href="https://github.com/CS211-661/cs211-661-project-homo-sapien/tree/main/src/main/java/cs211/project/models/collections">📂collections</a>                # responsible for managing collections of each models' object
┃ ┃ ┃ ┣ <a href="https://github.com/CS211-661/cs211-661-project-homo-sapien/tree/main/src/main/java/cs211/project/services">📂services</a>                     # responsible for performing background tasks
┃ ┃ ┃ ┗ 📜Main.java
┃ ┃ ┗ <a href="https://github.com/CS211-661/cs211-661-project-homo-sapien/tree/main/src/main/java/module-info.java">📜module-info.java</a>               # project dependencies
┃ ┗ <a href="https://github.com/CS211-661/cs211-661-project-homo-sapien/tree/main/src/main/resources">📂resources/211/project</a>            # stores user interface files (eg. styling, scenes, components)
┃ ┃ ┗ <a href="https://github.com/CS211-661/cs211-661-project-homo-sapien/tree/main/src/main/resources/cs211/project/views">📂views</a>
┣ <a href="https://github.com/CS211-661/cs211-661-project-homo-sapien/tree/main/src/main/uml-diagram">📂uml-diagram</a>                        # contains uml diagram for controllers, models, and services
┣ 📜.gitignore
┣ 📜pom.xml
┣ 📜README.md
┗ <a href="https://github.com/CS211-661/cs211-661-project-homo-sapien/blob/main/วิธีการใช้งานEventFiesta.pdf">📜วิธีการใช้งานEventFiesta.pdf</a>           # program manual
</pre>

---