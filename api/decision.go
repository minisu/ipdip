package api

import (
	uuid "github.com/satori/go.uuid"
	"time"
)

type Decision struct {
	Id            	string
	Name          	string
	Options       	[]string
	DecidedOption 	string
	DecidedAt		time.Time
}

type DecisionRepository interface {
	Get(id uuid.UUID) (Decision, error)
	Put(d Decision) error
}
