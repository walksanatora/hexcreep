package net.walksanator.hexcreep;

import at.petrak.hexcasting.api.casting.SpellList;
import at.petrak.hexcasting.api.casting.eval.vm.ContinuationFrame;
import at.petrak.hexcasting.api.casting.eval.vm.FrameEvaluate;
import at.petrak.hexcasting.api.casting.eval.vm.FrameForEach;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.ContinuationIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import net.walksanator.hexcreep.api.IEatIotas;
import net.walksanator.hexcreep.init.Blocks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public final class ExampleMod {
    public static final Logger LOGGER = LogManager.getLogger("hexcreep");
    public static final String MOD_ID = "hexcreep";

    public static final Predicate<Iota> DUPE_CHECK = (iota) -> {
        if (iota instanceof IEatIotas ie) {
            return ie.preventDupe();
        }
        return false;
    };
    public static final Predicate<Iota> STORE_CHECK = (iota) -> {
        if (iota instanceof IEatIotas ie) {
            return ie.preventStore();
        }
        return false;
    };

    public static void breakpoint() {
        LOGGER.info("BREAKPOINT!");
    }

    public static void init() {
        Blocks.INSTANCE.register();
        // Write common init code here.
    }

    public static boolean checkRecursive(Iota i, Predicate<Iota> check) {
        Iterable<Iota> subiotas = i.subIotas();
        if (subiotas != null) {
            for (Iota i2 : subiotas) {
                if (checkRecursive(i2,check)) {
                    return true;
                }
            }
        } else if (i instanceof ContinuationIota ci) {
            SpellContinuation continuation = ci.getContinuation();
            List<ContinuationFrame> frames = new LinkedList<>();
            while (continuation instanceof SpellContinuation.NotDone nd) {
                frames.add(nd.component1());
                continuation = nd.component2();
            }
            for (ContinuationFrame frame : frames) {
                if (frame instanceof FrameForEach each) {
                    for (Iota i3 : each.component1()) {
                        if (checkRecursive(i3,check)) {
                            return true;
                        }
                    }
                    for (Iota i3 : each.component2()) {
                        if (checkRecursive(i3,check)) {
                            return true;
                        }
                    }
                    List<Iota> oldStack = each.component3();
                    if (oldStack != null) {
                        for (Iota i3 : oldStack) {
                            if (checkRecursive(i3,check)) {
                                return true;
                            }
                        }
                    }
                    for (Iota i3 : each.component4()) {
                        if (checkRecursive(i3,check)) {
                            return true;
                        }
                    }
                }
                else if (frame instanceof FrameEvaluate eval) {
                    for (Iota i3 : eval.component1()) {
                        if (checkRecursive(i3,check)) {
                            return true;
                        }
                    }
                } else {
                    LOGGER.warn("Failed to check for subiotas of frame of class {}", frame.getClass().getName());
                }
            }

        } else {
            return check.test(i);
        }
        return false;
    }

    public static Iota mapRecursive(Iota i, Function<Iota,Iota> map) {
        if (i instanceof ListIota listIota) {
            List<Iota> accu = new LinkedList<>();
            for (Iota sub : listIota.getList()) {
                accu.add(mapRecursive(sub,map));
            }
            return new ListIota(accu);
        } else if (i instanceof ContinuationIota ci) {
            SpellContinuation continuation = ci.getContinuation();
            List<ContinuationFrame> frames = new LinkedList<>();
            while (continuation instanceof SpellContinuation.NotDone nd) {
                frames.add(nd.component1());
                continuation = nd.component2();
            }
            List<ContinuationFrame> remapped = new LinkedList<>();
            for (ContinuationFrame frame : frames) {
                if (frame instanceof FrameForEach thoth) {
                    List<Iota> component1 = new LinkedList<>();
                    for (Iota i2 : thoth.component1()) {
                        component1.add(
                                mapRecursive(i2,map)
                        );
                    }
                    List<Iota> component2 = new LinkedList<>();
                    for (Iota i2 : thoth.component2()) {
                        component2.add(
                                mapRecursive(i2,map)
                        );
                    }
                    List<Iota> component3 = null;
                    List<Iota> tcomponent3 = thoth.component3();
                    if (tcomponent3 != null) {
                        component3 = new LinkedList<>();
                        for (Iota i2 : tcomponent3) {
                            component3.add(
                                    mapRecursive(i2,map)
                            );
                        }
                    }
                    List<Iota> component4 = new LinkedList<>();
                    for (Iota i2 : thoth.component4()) {
                        component4.add(
                                mapRecursive(i2,map)
                        );
                    }
                    remapped.add(
                            new FrameForEach(new ListIota(component1).getList(), new ListIota(component2).getList(), component3, component4)
                    );
                } else if (frame instanceof FrameEvaluate hermes) {
                    List<Iota> component1 = new LinkedList<>();
                    for (Iota i2 : hermes.component1()) {
                        component1.add(mapRecursive(i2,map));
                    }
                    remapped.add(
                            new FrameEvaluate(new SpellList.LList(component1), hermes.component2())
                    );
                } else {
                    LOGGER.warn("no remapper defined for {}", frame.getClass().getName());
                    remapped.add(frame);
                }
            }

            for (ContinuationFrame rframe : remapped) {
                continuation = continuation.pushFrame(rframe);
            }

            return new ContinuationIota(continuation);
        } else {
            return map.apply(i);
        }
    }
}
