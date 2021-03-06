package com.airbnb.epoxy;

import android.support.annotation.VisibleForTesting;

import java.util.HashMap;
import java.util.Map;

class ViewTypeManager {
  private static Map<Class, Integer> viewTypeMap;
  /**
   * The last model that had its view type looked up. This is stored so in most cases we can quickly
   * look up what view type belongs to which model.
   */
  private EpoxyModel<?> lastModelForViewTypeLookup;

  /**
   * The type map is static so that models of the same class share the same views across different
   * adapters. This is useful for view recycling when the adapter instance changes, or when there
   * are multiple adapters. For testing purposes though it is good to be able to clear the map so we
   * don't carry over values across tests.
   */
  @VisibleForTesting
  void resetMapForTesting() {
    if (viewTypeMap != null) {
      viewTypeMap.clear();
    }
  }

  int getViewType(EpoxyModel<?> model) {
    lastModelForViewTypeLookup = model;
    return getViewTypeInternal(model);
  }

  private static int getViewTypeInternal(EpoxyModel<?> model) {
    int defaultViewType = model.getViewType();
    if (defaultViewType != 0) {
      return defaultViewType;
    }

    // If a model does not specify a view type then we generate a value to use for models of that
    // class.
    Class modelClass = model.getClass();
    if (viewTypeMap == null) {
      viewTypeMap = new HashMap<>();
    }

    Integer viewType = viewTypeMap.get(modelClass);

    if (viewType == null) {
      viewType = -viewTypeMap.size() - 1;
      viewTypeMap.put(modelClass, viewType);
    }

    return viewType;
  }

  /**
   * Find the model that has the given view type so we can create a view for that model. In most
   * cases this value is a layout resource and we could simply inflate it, but to support {@link
   * EpoxyModelWithView} we can't assume the view type is a layout. In that case we need to lookup
   * the model so we can ask it to create a new view for itself.
   * <p>
   * To make this efficient, we rely on the RecyclerView implementation detail that {@link
   * BaseEpoxyAdapter#getItemViewType(int)} is called immediately before {@link
   * BaseEpoxyAdapter#onCreateViewHolder(android.view.ViewGroup, int)} . We cache the last model
   * that had its view type looked up, and unless that implementation changes we expect to have a
   * very fast lookup for the correct model.
   * <p>
   * To be safe, we fallback to searching through all models for a view type match. This is slow and
   * shouldn't be needed, but is a guard against recyclerview behavior changing.
   */
  EpoxyModel<?> getModelForViewType(BaseEpoxyAdapter adapter, int viewType) {
    if (lastModelForViewTypeLookup != null
        && getViewTypeInternal(lastModelForViewTypeLookup) == viewType) {
      // We expect this to be a hit 100% of the time
      return lastModelForViewTypeLookup;
    }

    adapter.onExceptionSwallowed(
        new IllegalStateException("Last model did not match expected view type"));

    // To be extra safe in case RecyclerView implementation details change...
    for (EpoxyModel<?> model : adapter.getCurrentModels()) {
      if (model.getViewType() == viewType) {
        return model;
      }
    }

    // Check for the hidden model.
    HiddenEpoxyModel hiddenEpoxyModel = new HiddenEpoxyModel();
    if (viewType == hiddenEpoxyModel.getViewType()) {
      return hiddenEpoxyModel;
    }

    throw new IllegalStateException("Could not find model for view type: " + viewType);
  }
}
