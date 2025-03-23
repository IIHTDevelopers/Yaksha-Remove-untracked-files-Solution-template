* to execute and run test cases

  mvn clean install exec:java -Dexec.mainClass="mainapp.MyApp" -DskipTests=true

git config --global user.email ""
git config --global user.name ""
-------------------------------------------------------------------------------------------------

echo "first line" > untracked.txt
git add untracked.txt
git commit -m "add untracked.txt"

git reset untracked.txt
git rm --cached untracked.txt
git clean -f untracked.txt

