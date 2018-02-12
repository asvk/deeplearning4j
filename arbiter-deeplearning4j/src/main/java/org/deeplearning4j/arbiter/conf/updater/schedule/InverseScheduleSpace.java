package org.deeplearning4j.arbiter.conf.updater.schedule;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.deeplearning4j.arbiter.optimize.api.ParameterSpace;
import org.deeplearning4j.arbiter.optimize.parameter.FixedValue;
import org.nd4j.linalg.schedule.ExponentialSchedule;
import org.nd4j.linalg.schedule.ISchedule;
import org.nd4j.linalg.schedule.InverseSchedule;
import org.nd4j.linalg.schedule.ScheduleType;
import org.nd4j.shade.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor //JSON
@Data
public class InverseScheduleSpace implements ParameterSpace<ISchedule> {

    private ScheduleType scheduleType;
    private ParameterSpace<Double> initialValue;
    private ParameterSpace<Double> gamma;
    private ParameterSpace<Double> power;

    public InverseScheduleSpace(ScheduleType scheduleType, ParameterSpace<Double> initialValue, double gamma, double power){
        this(scheduleType, initialValue, new FixedValue<>(gamma), new FixedValue<>(power));
    }

    public InverseScheduleSpace(@JsonProperty("scheduleType") ScheduleType scheduleType,
                                @JsonProperty("initialValue") ParameterSpace<Double> initialValue,
                                @JsonProperty("gamma") ParameterSpace<Double> gamma,
                                @JsonProperty("power") ParameterSpace<Double> power){
        this.scheduleType = scheduleType;
        this.initialValue = initialValue;
        this.gamma = gamma;
        this.power = power;
    }

    @Override
    public ISchedule getValue(double[] parameterValues) {
        return new InverseSchedule(scheduleType, initialValue.getValue(parameterValues),
                gamma.getValue(parameterValues), power.getValue(parameterValues));
    }

    @Override
    public int numParameters() {
        return initialValue.numParameters() + gamma.numParameters() + power.numParameters();
    }

    @Override
    public List<ParameterSpace> collectLeaves() {
        return Arrays.<ParameterSpace>asList(initialValue, gamma, power);
    }

    @Override
    public Map<String, ParameterSpace> getNestedSpaces() {
        Map<String,ParameterSpace> out = new LinkedHashMap<>();
        out.put("initialValue", initialValue);
        out.put("gamma", gamma);
        out.put("power", power);
        return out;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public void setIndices(int... indices) {
        if(initialValue.numParameters() > 0){
            int[] sub = Arrays.copyOfRange(indices, 0, initialValue.numParameters());
            initialValue.setIndices(sub);
        }
        if(gamma.numParameters() > 0){
            int inp = initialValue.numParameters();
            int[] sub = Arrays.copyOfRange(indices, inp, inp + gamma.numParameters());
            gamma.setIndices(sub);
        }
        if(power.numParameters() > 0){
            int np = initialValue.numParameters() + gamma.numParameters();
            int[] sub = Arrays.copyOfRange(indices, np, np + power.numParameters());
            power.setIndices(sub);
        }
    }
}
