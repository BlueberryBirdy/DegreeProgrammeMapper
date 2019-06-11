package com.icecube.degreeprogrammemapper;

public enum Pathway {
    CORE,
    NETWORKING,
    SOFTWARE,
    DATABASE,
    MULTIMEDIA_WEB;

    @Override
    public String toString() {
        String result = "";
        switch (ordinal())
        {
            case 0:
                result = "Core";
                break;
            case 1:
                result = "Networking Engineering";
                break;
            case 2:
                result = "Software Engineering";
                break;
            case 3:
                result = "Database Architecture";
                break;
            case 4:
                result = "Multimedia and Web Development";
                break;
        }
        return result;
    }
}
