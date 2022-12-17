#!/bin/bash
for i in {1..1}
do
   echo "Creating PR # $i"
   git checkout main
   git pull
   git checkout -b stress-test-pipeline/stp-$i
   git commit --allow-empty -m "stress test pipeline $i"
   git push --set-upstream origin stress-test-pipeline/stp-$i
   gh pr create -t "stress test pipeline $i" -b "stress test pipeline $i"
   git checkout main
   git stash
   git stash drop
   git branch -D stress-test-pipeline/stp-$i
done