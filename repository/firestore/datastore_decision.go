package firestore

import (
	"cloud.google.com/go/datastore"
	"context"
	"github.com/minisu/ipdip/api"
	uuid "github.com/satori/go.uuid"
)

type DecisionRepo struct {
	client *datastore.Client
	ctx    context.Context
}

func NewFirestoreDecisionRepo(client *datastore.Client, ctx context.Context) *DecisionRepo {
	return &DecisionRepo{
		client: client,
		ctx:    ctx,
	}
}

func (r *DecisionRepo) Get(id uuid.UUID) (d api.Decision, err error) {
	key := datastore.NameKey("Decision", id.String(), nil)
	err = r.client.Get(r.ctx, key, &d)
	if err != nil {
		return d, err
	}
	return
}

func (r *DecisionRepo) Put(d api.Decision) (err error) {
	key := datastore.NameKey("Decision", d.Id, nil)
	_, err = r.client.Put(r.ctx, key, &d)
	return
}
