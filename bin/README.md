# semviz

## Main objective
Create a viewer for semantically annotated three-dimensional point clouds.

## Description
### Semantic visualization
Semantic visualization consists of displaying the elements contained within an ontology. This ontology can be written using an `.owl` file or a triple store. The ontology is susceptible of containing a large number of individuals, so a SPARQL interpretor needs to be implemented in order to allow the user to reduce the number of individuals on display through filtering. By default, the displayed individuals will be those of class `Object` in the ontology. When one or more individual are selected, their information as well as their representation in the framework will be displayed in the 3D section of the interface.

### 3D visualization
The display of a 3D object must be done in three ways:

- Low resolution display in cubic form (octree)
- Polygon display (convex hull)
- High resolution (base point cloud)
