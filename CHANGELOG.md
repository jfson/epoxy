# 2.0.0 (March 25, 2017)

* **New**: The `EpoxyController` class helps you manage even models better. This should be used instead of the original `EpoxyAdapter` in most places. Read more about `EpoxyController` in [the wiki](https://github.com/airbnb/epoxy/wiki/Epoxy-Controller).
* **Change**: In the new EpoxyController, the diffing algorithm uses both `equals` and `hashCode` on each model to check for changes. This is a change from the EpoxyAdapter where only `hashCode` was used. Generated models have both hashCode and equals implemented properly already, but if you have any custom hashCode implementations in your models make sure you have equals implemented as well.
* **New**: Models that have a `View.OnClickListener` as an EpoxyAttribute will now have an overloaded setter on the generated model that allows you to set a click listener that will return the model, view, and adapter position. **Upgrade Note** If you were setting a click listener value to null anywhere you will need to now cast that to `View.OnClickListener` because of the new overloaded method.
* **New**: Attach an onBind/onUnbind listener directly to a model instead of overriding the onModelBound method. Generated models will have methods created to set this listener and handle the callback for you.
* **New**: Support for creating models in Kotlin (Thanks to @geralt-encore! https://github.com/airbnb/epoxy/pull/144)
* **New**: `EpoxyModelWithView` supports creating a View programmatically instead of inflating from XML.
* **New**: `EpoxyModelGroup` supports grouping models together in arbitrary formations.
* **New**: Instead of setting attribute options like `@EpoxyAttribute(hash = false)` you should now do `@EpoxyAttribute(DoNotHash)`. You can also set other options like that.
* **New**: Annotation processor options can now be set via gradle instead of with `PackageEpoxyConfig`
* **New**: In an EpoxyController, if a model with the same id changes state Epoxy will include its previous state as a payload in the change notification. The new model will have its `bind(view, previouslyBoundModel)` method called so it can compare what changed since the previous model, and so it can update the view with only the data that changed.

# 1.7.5 (Feb 21, 2017)

* **New**: Models inherit layouts specified in superclass `@EpoxyModelClass` annotations [#119](https://github.com/airbnb/epoxy/pull/119)
* **New**: Support module configuration options [#124](https://github.com/airbnb/epoxy/pull/124)

# 1.6.2 (Feb 8, 2017)

* New: Support layout resource annotations in library projects (https://github.com/airbnb/epoxy/pull/116)

# 1.6.1 (Feb 6, 2017)

* Allow the default layout resource to be specified in the EpoxyModelClass class annotation [(#109)](https://github.com/airbnb/epoxy/pull/109) [(#111)](https://github.com/airbnb/epoxy/pull/111)
* Allow the `createNewHolder` method to be omitted and generated automatically [(#105)](https://github.com/airbnb/epoxy/pull/105)
* Generate a subclass for abstract model classes if the EpoxyModelClass annotation is present [(#105)](https://github.com/airbnb/epoxy/pull/105)
* Allow strings as model ids [(#107)](https://github.com/airbnb/epoxy/pull/107)
* Add instructions to readme for avoiding memory leaks [(#106)](https://github.com/airbnb/epoxy/pull/106)
* Add model callbacks for view attached/detached from window, and onFailedToRecycleView [(#104)](https://github.com/airbnb/epoxy/pull/104)
* Improve documentation on model unbind behavior [(#103)](https://github.com/airbnb/epoxy/pull/103)
* Fix generated methods from super classes that have var args [(#100)](https://github.com/airbnb/epoxy/pull/100)
* Remove apt dependency [(#95)](https://github.com/airbnb/epoxy/pull/95)
* Add `removeAllModels` method to EpoxyAdapter [(#94)](https://github.com/airbnb/epoxy/pull/94)
* Use actual param names when generating methods from super classes [(#85)](https://github.com/airbnb/epoxy/pull/85)

# 1.5.0 (11/21/2016)

* Fixes models being used in separate modules
* Generates a `reset()` method on each model to reset annotated fields to their defaults.
* Changes `@EpoxyAttribute(hash = false)` to still differentiate between null and non null values in the hashcode implementation
* Adds a `notifyModelChanged` method to EpoxyAdapter that allows a payload to be specified
* Generates a `toString()` method on all generated model classes that includes the values of all annotated fields.

# 1.4.0 (10/13/2016)

* Optimizations to the diffing algorithm
* Setters on generated classes are not created if an @EpoxyAttribute field is marked as `final`
* Adds @EpoxyModelClass annotation to force a model to have a generated class, even if it doesn't have any @EpoxyAttribute fields
* Fix to not generate methods for package private @EpoxyAttribute fields that are in a different package from the generated class
* Have generated classes duplicate any super methods that have the model as the return type to help with chaining

# 1.3.0 (09/15/2016)

* Add support for using the view holder pattern with models. See the readme for more information.
* Throw an exception if `EpoxyAdapter#notifyDataSetChanged()` is called when diffing is enabled. It doesn't make sense to allow this alongside diffing, and calling this is most likely to be an accidental mixup with `notifyModelsChanged()`.
* Some performance improvements with the diffing algorithm.

# 1.2.0 (09/07/2016)

* Change signature of `EpoxyAdapter#onModelBound` to include the model position
* Fix EpoxyModel hashcode to include the layout specified by `getDefaultLayout`
* Enforce that the id of an `EpoxyModel` cannot change once it has been added to the adapter
* Add optional hash parameter to the `EpoxyAttribute` annotation to exclude a field from being included in the generated hashcode method.

# 1.1.0 (08/24/2016)

* Initial release
