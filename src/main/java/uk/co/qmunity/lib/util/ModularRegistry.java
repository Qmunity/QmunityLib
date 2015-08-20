package uk.co.qmunity.lib.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModAPIManager;

public class ModularRegistry<T> implements Iterable<T> {

    private List<T> objects = new ArrayList<T>();

    @SuppressWarnings("unchecked")
    public <J extends T> J register(ModularRegistry.Dependency dep, Class<J> object) {

        return (J) register(dep, object, null);
    }

    public T register(ModularRegistry.Dependency dep, Class<? extends T> object, Class<? extends T> alt) {

        if (!dep.isAvailable())
            return null;

        try {
            T obj = null;
            if (object != null)
                obj = object.newInstance();
            else if (alt != null)
                obj = alt.newInstance();

            objects.add(obj);
            return obj;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public List<T> getRegisteredObjects() {

        return Collections.unmodifiableList(objects);
    }

    @Override
    public Iterator<T> iterator() {

        return getRegisteredObjects().iterator();
    }

    public static abstract class PrecompiledDependency<O> {

        private PrecompiledDependency() {

        }

        public abstract Dependency on(O dep);
    }

    public static abstract class Dependency {

        public static final PrecompiledDependency<String> MOD = new PrecompiledDependency<String>() {

            @Override
            public ModularRegistry.Dependency on(final String dep) {

                return new ModularRegistry.Dependency() {

                    @Override
                    public boolean isAvailable() {

                        return Loader.isModLoaded(dep);
                    }
                };
            }
        };
        public static final PrecompiledDependency<String> API = new PrecompiledDependency<String>() {

            @Override
            public ModularRegistry.Dependency on(final String dep) {

                return new ModularRegistry.Dependency() {

                    @Override
                    public boolean isAvailable() {

                        return ModAPIManager.INSTANCE.hasAPI(dep);
                    }
                };
            }
        };
        public static final PrecompiledDependency<String> CLASS = new PrecompiledDependency<String>() {

            @Override
            public ModularRegistry.Dependency on(final String dep) {

                return new ModularRegistry.Dependency() {

                    @Override
                    public boolean isAvailable() {

                        try {
                            Class.forName(dep);
                            return true;
                        } catch (Exception ex) {
                        }
                        return false;
                    }
                };
            }
        };
        public static final Dependency NONE = new Dependency() {

            @Override
            public boolean isAvailable() {

                return true;
            }

        };

        public abstract boolean isAvailable();

        public Dependency or(Dependency dependency) {

            if (this instanceof AnyConditionDependency) {
                ((AnyConditionDependency) this).add(dependency);
                return this;
            } else {
                return new AnyConditionDependency(this, dependency);
            }
        }

        public Dependency and(Dependency dependency) {

            if (this instanceof MultiConditionDependency) {
                ((MultiConditionDependency) this).add(dependency);
                return this;
            } else {
                return new MultiConditionDependency(this, dependency);
            }
        }

        public Dependency negate() {

            return new InvertedDependency(this);
        }
    }

    public static class MultiConditionDependency extends Dependency {

        private final Set<Dependency> deps = new HashSet<Dependency>();

        public MultiConditionDependency(Dependency... deps) {

            this.deps.addAll(Arrays.asList(deps));
        }

        public MultiConditionDependency add(Dependency dep) {

            deps.add(dep);
            return this;
        }

        @Override
        public boolean isAvailable() {

            if (deps.size() == 0)
                return false;

            for (Dependency dep : deps)
                if (!dep.isAvailable())
                    return false;

            return true;
        }

    }

    public static class AnyConditionDependency extends Dependency {

        private final Set<Dependency> deps = new HashSet<Dependency>();

        public AnyConditionDependency(Dependency... deps) {

            this.deps.addAll(Arrays.asList(deps));
        }

        public AnyConditionDependency add(Dependency dep) {

            deps.add(dep);
            return this;
        }

        @Override
        public boolean isAvailable() {

            for (Dependency dep : deps)
                if (dep.isAvailable())
                    return true;

            return false;
        }

    }

    public static class InvertedDependency extends Dependency {

        private final Dependency dependency;

        public InvertedDependency(Dependency dependency) {

            this.dependency = dependency;
        }

        @Override
        public boolean isAvailable() {

            return !dependency.isAvailable();
        }

    }

}
