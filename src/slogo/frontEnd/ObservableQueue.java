package slogo.frontEnd;

import javafx.collections.ModifiableObservableListBase;

import java.util.LinkedList;
import java.util.List;

public class ObservableQueue extends ModifiableObservableListBase<String> {

    private final List<String> delegate = new LinkedList<>();

    @Override
    public String get(int index) {
        return delegate.get(index);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    /**
     * Adds the {@code element} to the List at the end of the list.
     *
     * <p>For the description of possible exceptions, please refer to the documentation
     * of {@link #add(Object) } method.
     *
     * @param index   irrelevant
     * @param element the element that will be added
     * @throws ClassCastException        if the type of the specified element is
     *                                   incompatible with this list
     * @throws NullPointerException      if the specified arguments contain one or
     *                                   more null elements
     * @throws IllegalArgumentException  if some property of this element
     *                                   prevents it from being added to this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   {@code (index < 0 || index > size())}
     */
    @Override
    protected void doAdd(int index, String element) {
        delegate.add(element);
    }

    /**
     * Sets the {@code element} in the List at the position of {@code index}.
     *
     * <p>For the description of possible exceptions, please refer to the documentation
     * of {@link #set(int, Object) } method.
     *
     * @param index   the position where to set the element
     * @param element the element that will be set at the specified position
     * @return the old element at the specified position
     * @throws ClassCastException        if the type of the specified element is
     *                                   incompatible with this list
     * @throws NullPointerException      if the specified arguments contain one or
     *                                   more null elements
     * @throws IllegalArgumentException  if some property of this element
     *                                   prevents it from being added to this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   {@code (index < 0 || index >= size())}
     */
    @Override
    protected String doSet(int index, String element) {
        return null;
    }

    /**
     * Removes the first element.
     *
     * @param index irrelevant
     * @return the removed element
     */
    @Override
    protected String doRemove(int index) {
        return delegate.remove(0);
    }
}
