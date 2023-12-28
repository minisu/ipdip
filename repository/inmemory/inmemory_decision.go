package inmemory

import (
	"github.com/minisu/ipdip/api"
	uuid "github.com/satori/go.uuid"
)

type DecisionRepo struct {
	decisions map[string]api.Decision
}

func NewInMemoryDecisionRepo() *DecisionRepo {
	return &DecisionRepo{
		decisions: make(map[string]api.Decision),
	}
}

func (r *DecisionRepo) Get(id uuid.UUID) (d api.Decision, err error) {
	d = r.decisions[id.String()]
	return
}

func (r *DecisionRepo) Put(d api.Decision) (err error) {
	r.decisions[d.Id] = d
	return
}
