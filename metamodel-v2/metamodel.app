application metamodel

entity DomainApp {
  name : String
  entities : {DomainEntity} (inverse = application)
}

entity DomainEntity {
  name : String
  application -> DomainApp
  properties : {EntityProperty} (inverse = entity)
  relationships : {Relationship} (inverse = sourceEntity)
}

entity EntityProperty {
  name : String
  propertyType : String
  entity -> DomainEntity
}

entity Relationship {
  inverseName : String
  name : String
  relationshipType : String
  sourceEntity -> DomainEntity
  targetEntity -> DomainEntity
}

derive CRUD DomainApp
derive CRUD DomainEntity
derive CRUD EntityProperty
derive CRUD Relationship

page root() {
  title { "metamodel" }
  header { "Welcome" }
  par { navigate manageDomainApp() { "Manage DomainApps" } }
  par { navigate manageDomainEntity() { "Manage DomainEntitys" } }
  par { navigate manageEntityProperty() { "Manage EntityPropertys" } }
  par { navigate manageRelationship() { "Manage Relationships" } }
}
