package com.google.inject;

import com.google.inject.Key.AnnotationStrategy;
import com.google.inject.binder.ConstantBindingBuilder;

/**
   * Builds a constant binding.
 */
public class ConstantBindingBuilderImpl implements ConstantBindingBuilder {

  BindingInfo<?> bindingInfo;
  final AnnotationStrategy annotationStrategy;
  Object source = BinderImpl.UNKNOWN_SOURCE;
  private BinderImpl binder;

  ConstantBindingBuilderImpl(BinderImpl binder, AnnotationStrategy annotationStrategy) {
    this.binder = binder;
    this.annotationStrategy = annotationStrategy;
  }

  boolean hasValue() {
    return bindingInfo != null;
  }

  Object getSource() {
    return source;
  }

  ConstantBindingBuilderImpl from(Object source) {
    this.source = source;
    return this;
  }

  public void to(String value) {
    to(String.class, value);
  }

  public void to(int value) {
    to(int.class, value);
  }

  public void to(long value) {
    to(long.class, value);
  }

  public void to(boolean value) {
    to(boolean.class, value);
  }

  public void to(double value) {
    to(double.class, value);
  }

  public void to(float value) {
    to(float.class, value);
  }

  public void to(short value) {
    to(short.class, value);
  }

  public void to(char value) {
    to(char.class, value);
  }

  public void to(Class<?> value) {
    to(Class.class, value);
  }

  public <E extends Enum<E>> void to(E value) {
    to(value.getDeclaringClass(), value);
  }

  /**
   * Maps a constant value to the given type and name.
   */
  <T> void to(final Class<T> type, final T value) {
    if (this.bindingInfo != null) {
      binder.addError(source, ErrorMessages.CONSTANT_VALUE_ALREADY_SET);
    } else {
      this.bindingInfo
          = new BindingInfo<T>(type, value, annotationStrategy, source);
    }
  }

  Binding<?> createBinding(ContainerImpl container) {
    return bindingInfo.createBinding(container);
  }

  private static class BindingInfo<T> {

    final Class<T> type;
    final T value;
    final AnnotationStrategy annotationStrategy;
    final Object source;

    BindingInfo(Class<T> type, T value,
        AnnotationStrategy annotationStrategy, Object source) {
      this.type = type;
      this.value = value;
      this.annotationStrategy = annotationStrategy;
      this.source = source;
    }

    Binding<T> createBinding(ContainerImpl container) {
      Key<T> key = Key.get(type, annotationStrategy);
      ConstantFactory<T> factory = new ConstantFactory<T>(value);
      return Binding.newInstance(container, key, source, factory);
    }
  }
}
