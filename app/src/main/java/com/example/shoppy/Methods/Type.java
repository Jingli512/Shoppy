package com.example.shoppy.Methods;

public enum Type {
    UNITS,
    G,
    KG,
    ML,
    L,
    CUPS,
    PINCH,
    TBSP,
    TSP,
    SLICES;

    @Override
    public String toString()
    {
        if (this.equals(Type.L))
        {
            return super.toString();
        } else {
            return super.toString().toLowerCase();
        }
    }

    public static Type getType(String type)
    {
        switch (type)
        {
            case "units":
                return UNITS;
            case "g":
                return G;
            case "kg":
                return KG;
            case "ml":
                return ML;
            case "l":
            case "L":
                return L;
            case "cups":
                return CUPS;
            case "pinch":
                return PINCH;
            case "tbsp":
                return TBSP;
            case "tsp":
                return TSP;
            case "slices":
                return SLICES;
            default:
                throw new IllegalArgumentException("Invalid Argument");
        }
    }
}
