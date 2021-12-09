package main

import (
	"cloud.google.com/go/datastore"
	"context"
	"github.com/gin-gonic/gin"
	"github.com/minisu/ipdip/repository/firestore_repo"
	"github.com/satori/go.uuid"
	"log"
	"net/http"
	"os"
	"strings"
)

func main() {
	ctx := context.Background()
	dsClient, err := datastore.NewClient(ctx, "ipdip-334118")

	if err != nil {
		log.Fatalln(err)
		return
	}
	defer dsClient.Close()

	repository := firestore_repo.NewFirestoreDecisionRepo(dsClient, ctx)
	//repository := inmemory.NewInMemoryDecisionRepo()
	decisionMaker := NewDecisionMaker(repository)

	r := gin.Default()
	r.LoadHTMLGlob("templates/*")
	r.StaticFile("/", "./index.html")

	r.GET("/decision/:id", func(c *gin.Context) {
		decisionId, err := uuid.FromString(c.Param("id"))
		if err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
			return
		}

		d, err := decisionMaker.getDecision(decisionId)
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
			return
		}

		c.HTML(http.StatusOK, "decision.tmpl", d)
	})
	r.POST("/decision/:id/decide", func(c *gin.Context) {
		decisionId, err := uuid.FromString(c.Param("id"))
		if err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
			return
		}

		_, err = decisionMaker.decide(decisionId)
		if err != nil {
			c.String(400, err.Error())
			return
		}

		c.Redirect(302, "/decision/"+decisionId.String())
	})
	r.POST("/decision", func(c *gin.Context) {
		name := c.PostForm("name")
		options := strings.Split(c.PostForm("options"), "\n")

		id, err := decisionMaker.createDecision(name, options)
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		}
		c.Redirect(302, "/decision/"+id.String())
	})

	// Determine port for HTTP service.
	port := os.Getenv("PORT")
	if port == "" {
		port = "8080"
		log.Printf("defaulting to port %s", port)
	}

	r.Run(":" + port) // serve on localhost:8080
}
