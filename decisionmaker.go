package main

import (
	"fmt"
	"github.com/minisu/ipdip/api"
	"github.com/satori/go.uuid"
	"math/rand"
	"time"
)

type DecisionMaker struct {
	repository api.DecisionRepository
}

func NewDecisionMaker(repository api.DecisionRepository) *DecisionMaker {
	return &DecisionMaker{repository: repository}
}

func (m *DecisionMaker) createDecision(name string, options []string) (id uuid.UUID, err error) {
	id = uuid.NewV4()
	err = m.repository.Put(api.Decision{Id: id.String(), Name: name, Options: options})
	return
}

func (m *DecisionMaker) decide(id uuid.UUID) (d api.Decision, err error) {
	d, err = m.repository.Get(id)

	if err != nil {
		return
	}

	if d.DecidedOption != "" {
		return d, fmt.Errorf("decision already made")
	}

	decidedOption := pickRandom(d.Options)
	d.DecidedOption = decidedOption
	d.DecidedAt = time.Now().UTC()
	err = m.repository.Put(d)

	return
}

func (m *DecisionMaker) getDecision(id uuid.UUID) (d api.Decision, err error) {
	return m.repository.Get(id)
}

func pickRandom(elements []string) string {
	return elements[rand.Intn(len(elements))]
}
