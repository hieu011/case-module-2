package Services;





import model.Fruit;

import java.util.List;

public interface IFruitService {

    List<Fruit> getFruits();



    Fruit getFruitById(int id);


    void add(Fruit newDrug);

    void update(Fruit newDrug);

    void remove(Fruit drug);

    boolean isIdExisted(int id);

    List<Fruit> getSearchFruitList(String searchContent, List<Fruit> userDrugsList);

}

