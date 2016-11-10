package components.application;


import components.mapcanvas.IntersectionSelectionEvent;
import models.Intersection;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * This class represents the state of the application when it is waiting for
 * the user to choose an intersection on the map.
 * This state is entered by a request to the MapService and response
 * (Intersecion) is provided by resolving a future.
 */
public class SelectingIntersectionState extends WaitOpenDeliveryRequestState {
    /**
     * The future to resolve to respond to the intersection selection request.
     */
    final private CompletableFuture<Intersection> future;

    public SelectingIntersectionState(@NotNull MainController mainController, @NotNull CompletableFuture<Intersection> future) {
        super(mainController);
        this.future = future;
    }

    @Override
    public void enterState() {

    }

    @Override
    public void leaveState() {

    }

    /**
     * Resolve the future provided by the request and return to the
     * ComputedPlanningState state.
     * <p>
     * The IntersectionSelectionEvent is normally generated by the MapCanvas.
     *
     * @param event An event containing the selected intersection.
     * @return The next state (ComputedPlanningState) if the result was valid, otherwise it stays in this state.
     */
    @Override
    @NotNull
    public MainControllerState onIntersectionSelection(@NotNull IntersectionSelectionEvent event) {
        Intersection intersection = event.getIntersection();
        if (intersection == null) {
            return this;
        }
        mainController.modifyComputePlanningButtonDisabledProperty(true);
        this.future.complete(intersection);
        return new ComputedPlanningState(this.mainController);
    }
}
