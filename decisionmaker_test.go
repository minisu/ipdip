package main

import (
	"github.com/rahmanfadhil/gin-bookstore/repository/inmemory"
	"github.com/stretchr/testify/assert"
	"testing"
)

func Test_decisionMaker_create_and_get(t *testing.T) {
	m := decisionMaker { repository: inmemory.NewInMemoryDecisionRepo() }
	name := "Which flavor?"
	options := []string{"Vanilla", "Chocolate"}
	id, err := m.createDecision(name, options)

	if id == [16]byte{} || err != nil {
		t.Failed()
	}

	d, err := m.getDecision(id)

	assert.Equal(t, id, d.Id)
	assert.Equal(t, name, d.Name)
	assert.Equal(t, "", d.DecidedOption)
	assert.Equal(t, options, d.Options)
}

func Test_decisionMaker_decide(t *testing.T) {
	m := decisionMaker { repository: inmemory.NewInMemoryDecisionRepo() }
	name := "Which flavor?"
	options := []string{"Vanilla", "Chocolate"}
	id, err := m.createDecision(name, options)

	assert.Nil(t, err)

	d, err := m.decide(id)

	assert.Contains(t, options, d.DecidedOption)

	d, err = m.decide(id)

	assert.NotNil(t, err)

	d2, err := m.getDecision(id)

	assert.Equal(t, d.DecidedOption, d2.DecidedOption)
}

