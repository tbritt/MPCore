package com.monkeypantssoftware.core.statistics;

import java.util.List;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;

import er.extensions.foundation.ERXArrayUtilities;

public class Basic {

    public static double standardDeviation(List<? extends Number> list) {
        double mean = mean(list);
        double squaredDeviationsNumerator = 0d;
        for (Number aValue : list) {
            double deviation = mean - aValue.doubleValue();
            // square each of the deviations, which amplifies large deviations
            // and makes negative values positive and add them to the numerator
            squaredDeviationsNumerator = squaredDeviationsNumerator + (deviation * deviation);
        }
        double meanOfSquaredDeviations = squaredDeviationsNumerator / list.size();
        return Math.sqrt(meanOfSquaredDeviations);
    }

    public static double mean(List<? extends Number> list) {
        double numerator = 0d;
        double denominator = list.size();

        for (Number aValue : list) {
            numerator += aValue.doubleValue();
        }
        
        if (denominator == 0) return 0d;
        return numerator / denominator;
    }

    public static double median(List<? extends Number> list) {
        if (!(list instanceof NSArray<?>)) {
            list = new NSArray<Number>(list); 
        }
        return ERXArrayUtilities.median((NSArray<?>)list, "doubleValue").doubleValue();
    }
    
    /**
     * Returns percentile rank of the valueOfInterest among the list of values.
     * See http://en.wikipedia.org/wiki/Percentile_rank
     */
    public static Double percentileRank(List<? extends Number> values, Double valueOfInterest) {
        Double percentileRank = Double.valueOf(0);
        
        if (values.isEmpty()) return percentileRank;
        
        double frequencyOfLowerAvgs = 0;
        double frequencyOfIdenticalAvgs = 0;
        for (Number value : values) {
            if (value.doubleValue() < valueOfInterest.doubleValue()) {
                frequencyOfLowerAvgs++;
            }
            else if (value.doubleValue() == valueOfInterest.doubleValue()) {
                frequencyOfIdenticalAvgs++;
            }
        }
        
        percentileRank = Double.valueOf((frequencyOfLowerAvgs + (0.5d * frequencyOfIdenticalAvgs)) / (values.size()) * 100);
        return percentileRank;
    }

    public static NSArray<Double> mode(List<? extends Number> list) {
        NSMutableDictionary<Double, Integer> valueFrequencyMap = new NSMutableDictionary<Double, Integer>();
        NSMutableArray<Double> modes = new NSMutableArray<Double>();
        
        int maxFrequency = 0;
        for (Number value : list) {
            
            // Figure out how many times we've seen this value and update it.
            Integer frequency = valueFrequencyMap.objectForKey(value);
            if (frequency == null) {
                frequency = Integer.valueOf(1);
            }
            else {
                frequency = Integer.valueOf(frequency.intValue() + 1);
            }
            valueFrequencyMap.setObjectForKey(frequency, Double.valueOf(value.doubleValue()));
            
            // If we've seen this value more times than any other we should 
            // clear out any previous modes and set it as the new max frequency.
            if (frequency.intValue() > maxFrequency) {
                maxFrequency = frequency.intValue();
                modes.removeAllObjects();
            }
            
            // If the frequency of this value matches our max frequency we should
            // add it as a mode.
            if (frequency.intValue() == maxFrequency) {
                modes.add(Double.valueOf(value.doubleValue()));
            }
        }
        
        return modes.immutableClone();
    }
}
