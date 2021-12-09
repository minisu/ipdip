package inmemory

import (
	"github.com/minisu/ipdip/api"
	uuid "github.com/satori/go.uuid"
)

type inMemoryDecisionRepo struct {
	decisions map[string]api.Decision
}

func NewInMemoryDecisionRepo() *inMemoryDecisionRepo {
	return &inMemoryDecisionRepo{
		decisions: make(map[string]api.Decision), // is it necessary to call make here or will it be the default?
	}
}

func (r *inMemoryDecisionRepo) Get(id uuid.UUID) (d api.Decision, err error) {
	d = r.decisions[id.String()]
	return
}

func (r *inMemoryDecisionRepo) Put(d api.Decision) (err error) {
	r.decisions[d.Id] = d
	return
}
