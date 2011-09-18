package magic.model.mstatic;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Collection;

import magic.model.MagicCopyMap;
import magic.model.MagicPermanent;

public class MagicPermanentStaticMap {
    
    private final Map<MagicLayer,SortedSet<MagicPermanentStatic>> effects = 
        new EnumMap<MagicLayer,SortedSet<MagicPermanentStatic>>(MagicLayer.class);
	
	public MagicPermanentStaticMap() {
        for (MagicLayer layer : MagicLayer.values()) {
            effects.put(layer, new TreeSet<MagicPermanentStatic>());
        }
        //changes to power and toughness due to +1/+1 and -1/-1 counters
        add(MagicPermanentStatic.CountersEffect);
    }
	
    public MagicPermanentStaticMap(final MagicCopyMap copyMap, final MagicPermanentStaticMap sourceMap) {
        for (MagicLayer layer : MagicLayer.values()) {
            effects.put(layer, new TreeSet<MagicPermanentStatic>());
        }
	    
        for (final Map.Entry<MagicLayer, SortedSet<MagicPermanentStatic>> layer : sourceMap.effects.entrySet()) {
            for (final MagicPermanentStatic mpstatic : layer.getValue()) {
                add(new MagicPermanentStatic(copyMap, mpstatic));
            }
        }
    }

    public SortedSet<MagicPermanentStatic> get(final MagicLayer layer) {
        return effects.get(layer);
    }

    public void add(final MagicPermanentStatic mpstatic) {
        effects.get(mpstatic.getLayer()).add(mpstatic);
    }
    
    public void remove(final MagicPermanent permanent,final Collection<MagicPermanentStatic> removedStatics) {
	    for (final Map.Entry<MagicLayer, SortedSet<MagicPermanentStatic>> layer : effects.entrySet()) {
            final Collection<MagicPermanentStatic> statics = layer.getValue();
            for (final Iterator<MagicPermanentStatic> iterator = statics.iterator();iterator.hasNext();) {
                final MagicPermanentStatic permanentStatic = iterator.next();
                if (permanentStatic.getPermanent() == permanent) {
                    iterator.remove();
                    removedStatics.add(permanentStatic);
                }
            }
        }
    }
        
    public void remove(final MagicPermanent permanent, final MagicStatic mstatic) {
        final Collection<MagicPermanentStatic> statics = effects.get(mstatic.getLayer());
        for (final Iterator<MagicPermanentStatic> iterator = statics.iterator();iterator.hasNext();) {
            final MagicPermanentStatic permanentStatic = iterator.next();
            if (permanentStatic.getPermanent() == permanent && permanentStatic.getStatic() == mstatic) {
                iterator.remove();
            }
        }
    }
}
