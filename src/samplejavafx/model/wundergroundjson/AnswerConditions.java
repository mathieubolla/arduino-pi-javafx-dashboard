package samplejavafx.model.wundergroundjson;

public class AnswerConditions {
    public AnswerConditions(Response response, Conditions current_observation) {
        this.response = response;
        this.current_observation = current_observation;
    }
    
    private Response response;
    private Conditions current_observation;

    public Conditions getConditions() {
        return current_observation;
    }

    public Response getResponse() {
        return response;
    }
}
