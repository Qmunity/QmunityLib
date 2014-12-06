package uk.co.qmunity.lib.transform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;

public class TransformationList implements List<Transformation>, Transformation {

    private List<Transformation> l = new ArrayList<Transformation>();

    public TransformationList(Transformation... transformations) {

        l.addAll(Arrays.asList(transformations));
    }

    public TransformationList(List<Transformation> list) {

        l.addAll(list);
    }

    @Override
    public boolean add(Transformation transformation) {

        return l.add(transformation);
    }

    @Override
    public void add(int index, Transformation transformation) {

        l.add(index, transformation);
    }

    @Override
    public boolean addAll(Collection<? extends Transformation> collection) {

        return l.addAll(collection);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Transformation> collection) {

        return l.addAll(index, collection);
    }

    @Override
    public void clear() {

        l.clear();
    }

    @Override
    public boolean contains(Object object) {

        return l.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {

        return l.containsAll(collection);
    }

    @Override
    public Transformation get(int index) {

        return l.get(index);
    }

    @Override
    public int indexOf(Object object) {

        return l.indexOf(object);
    }

    @Override
    public boolean isEmpty() {

        return l.isEmpty();
    }

    @Override
    public Iterator<Transformation> iterator() {

        return l.iterator();
    }

    @Override
    public int lastIndexOf(Object object) {

        return l.lastIndexOf(object);
    }

    @Override
    public ListIterator<Transformation> listIterator() {

        return l.listIterator();
    }

    @Override
    public ListIterator<Transformation> listIterator(int index) {

        return l.listIterator(index);
    }

    @Override
    public boolean remove(Object object) {

        return l.remove(object);
    }

    @Override
    public Transformation remove(int index) {

        return l.remove(index);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {

        return l.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {

        return l.retainAll(collection);
    }

    @Override
    public Transformation set(int index, Transformation transformation) {

        return l.set(index, transformation);
    }

    @Override
    public int size() {

        return l.size();
    }

    @Override
    public TransformationList subList(int fromIndex, int toIndex) {

        TransformationList l = new TransformationList();
        l.addAll(l.subList(fromIndex, toIndex));
        return l;
    }

    @Override
    public Object[] toArray() {

        return l.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {

        return l.toArray(a);
    }

    @Override
    public Vec3d apply(Vec3d point) {

        point = point.clone();

        List<Transformation> list = new ArrayList<Transformation>(l);
        Collections.reverse(list);
        for (Transformation t : list)
            point = t.apply(point);

        return point;
    }

    @Override
    public Vec3dCube apply(Vec3dCube cube) {

        cube = cube.clone();

        List<Transformation> list = new ArrayList<Transformation>(l);
        Collections.reverse(list);
        for (Transformation t : list)
            cube = t.apply(cube);

        return cube;
    }

}
