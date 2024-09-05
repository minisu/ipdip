package main

import (
	"cloud.google.com/go/datastore"
	"context"
	"github.com/minisu/ipdip/repository/inmemory"
	"github.com/satori/go.uuid"
	"html/template"
	"log"
	"net/http"
	"os"
	"strings"
)

func main() {
	ctx := context.Background()
	dsClient, err := datastore.NewClient(ctx, "ipdip-334118")

	decisionTmpl := template.Must(template.ParseFiles("templates/decision.gohtml"))

	if err != nil {
		log.Fatalln(err)
		return
	}
	defer dsClient.Close()

	//repository := firestore.NewFirestoreDecisionRepo(dsClient, ctx)
	repository := inmemory.NewInMemoryDecisionRepo()
	decisionMaker := NewDecisionMaker(repository)

	http.HandleFunc("GET /{$}", func(w http.ResponseWriter, r *http.Request) {
		http.ServeFile(w, r, "index.html")
	})

	http.HandleFunc("GET /decision/{id}", func(w http.ResponseWriter, r *http.Request) {
		decisionId, err := uuid.FromString(r.PathValue("id"))
		if err != nil {
			http.Error(w, err.Error(), http.StatusNotFound)
			return
		}

		d, err := decisionMaker.getDecision(decisionId)
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}

		decisionTmpl.Execute(w, d)
	})

	http.HandleFunc("POST /decision/{id}/decide", func(w http.ResponseWriter, r *http.Request) {
		decisionId, err := uuid.FromString(r.PathValue("id"))
		if err != nil {
			http.Error(w, err.Error(), http.StatusBadRequest)
			return
		}

		_, err = decisionMaker.decide(decisionId)
		if err != nil {
			http.Error(w, err.Error(), http.StatusBadRequest)
			return
		}

		http.Redirect(w, r, "/decision/"+decisionId.String(), http.StatusFound)
	})

	http.HandleFunc("POST /decision", func(w http.ResponseWriter, r *http.Request) {
		if err := r.ParseForm(); err != nil {
			http.Error(w, err.Error(), http.StatusBadRequest)
			return
		}

		name := r.PostForm["name"][0]
		options := strings.Split(r.PostForm["options"][0], "\n")

		id, err := decisionMaker.createDecision(name, options)
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}
		http.Redirect(w, r, "/decision/"+id.String(), http.StatusFound)
	})

	// Determine port for HTTP service.
	port := os.Getenv("PORT")
	if port == "" {
		port = "8080"
		log.Printf("defaulting to port %s", port)
	}

	log.Printf("Listening on :%s...", port)
	if err := http.ListenAndServe(":"+port, http.DefaultServeMux); err != nil {
		log.Fatal(err)
	}
}
