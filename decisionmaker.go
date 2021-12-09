package main

import (
	"fmt"
	"github.com/minisu/ipdip/api"
	uuid "github.com/satori/go.uuid"
	"math/rand"
)

type decisionMaker struct {
	repository api.DecisionRepository
}

func NewDecisionMaker(repository api.DecisionRepository) *decisionMaker {
	return &decisionMaker{repository: repository}
}

func (m *decisionMaker) createDecision(name string, options []string) (id uuid.UUID, err error) {
	id = uuid.NewV4()
	err = m.repository.Put(api.Decision{Id: id.String(), Name: name, Options: options})
	return
}

func (m *decisionMaker) decide(id uuid.UUID) (d api.Decision, err error) {
	d, err = m.repository.Get(id)

	if err != nil {
		return
	}

	if d.DecidedOption != "" {
		return d, fmt.Errorf("Decision already made")
	}

	decidedOption := pickRandom(d.Options)
	d.DecidedOption = decidedOption
	err = m.repository.Put(d)

	return
}

func (m *decisionMaker) getDecision(id uuid.UUID) (d api.Decision, err error) {
	return m.repository.Get(id)
}

func pickRandom(elements []string) string {
	return elements[rand.Intn(len(elements))]
}
