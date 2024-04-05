package ms.cs.countries_quiz;

public class Neighbours {
    private long   id;
    private String country_name;
    private String neighbour_name;

    public Neighbours(){
        this.id = -1;
        this.country_name = null;
        this.neighbour_name = null;
    }

    public Neighbours(String country_name, String neighbour_name){
        this.id = -1;
        this.country_name = country_name;
        this.neighbour_name = neighbour_name;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getCountryName()
    {
        return country_name;
    }

    public void setCountryName(String country_name)
    {
        this.country_name = country_name;
    }

    public String getNeighbourName()
    {
        return neighbour_name;
    }

    public void setNeighbourName(String neighbour_name)
    {
        this.neighbour_name = neighbour_name;
    }

    public String toString()
    {
        return id + ": " + country_name + " " + neighbour_name;
    }

}
