# Ipdip

Lets a group make random decisions remotely. Create a decision and share the link to the rest of the group. Once everyone has seen the (undecided) decision, click Decide randomly.

Ipdip is written in Go, stores its data in Datastore and deployed as a Google Cloud Function.

## Run
```
$ go build
$ ./ipdip
```

## Deploy as Cloud Function
```
$ gcloud run deploy
```
