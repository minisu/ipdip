# Ipdip

Lets a group make random decisions remotely. Create a decision and share the link to the rest of the group. Once everyone has seen the (undecided) decision, click Decide.

Ipdip is written in Go, stores its data in Datastore and deployed in Google Cloud Run.

https://ipdip-tue5z4re6q-lz.a.run.app

## Run
```
$ go build
$ ./ipdip
```

## Deploy on Cloud Run
```
$ gcloud run deploy
```
