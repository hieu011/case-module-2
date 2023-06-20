package Services;




import model.Fruit;
import utils.CSVUtils;

import java.util.ArrayList;
import java.util.List;

public class FruitService implements IFruitService {
    public static String path = "C:\\Users\\LAPTOP\\IdeaProjects\\casemodule2\\src\\main\\java\\datas\\fruits.txt";

    @Override
    public List<Fruit> getFruits() {
        List<Fruit> newDrugsList = new ArrayList<>();
        List<String> records = CSVUtils.readData(path);
        for (String record : records) {
            newDrugsList.add(new Fruit(record));
        }
        return newDrugsList;
    }



    @Override
    public Fruit getFruitById(int id) {
        List<Fruit> drugs = getFruits();
        for (Fruit drug : drugs) {
            if (drug.getId() == id)
                return drug;
        }
        return null;
    }


    @Override
    public void add(Fruit newDrug) {
        List<Fruit> drugs = getFruits();
        drugs.add(newDrug);
        CSVUtils.writeData(path, drugs);
    }

    @Override
    public void update(Fruit newFruit) {
        List<Fruit> fruits = getFruits();
        for (Fruit drug : fruits) {
            if (drug.getId() == newFruit.getId()) {
                if (newFruit.getFruitName() != null && !newFruit.getFruitName().isEmpty())
                    drug.setFruitName(newFruit.getFruitName());

                if (newFruit.getQuantity() != drug.getQuantity())
                    drug.setQuantity(newFruit.getQuantity());

                if (newFruit.getPricePerPill() != drug.getPricePerPill())
                    drug.setPricePerPill(newFruit.getPricePerPill());

                if (newFruit.getProductionDate() != null && !newFruit.getProductionDate().isEmpty())
                    drug.setProductionDate(newFruit.getProductionDate());

                if (newFruit.getExpirationDate() != null && !newFruit.getExpirationDate().isEmpty())
                    drug.setExpirationDate(newFruit.getExpirationDate());

                if (newFruit.getNote() != null && !newFruit.getNote().isEmpty())
                    drug.setNote(newFruit.getNote());

                CSVUtils.writeData(path, fruits);
                break;
            }
        }
    }

    @Override
    public boolean isIdExisted(int id) {
        return getFruitById(id) != null;
    }

    @Override
    public void remove(Fruit drug) {
        List<Fruit> drugs = getFruits();
        drugs.remove(drug);
        CSVUtils.writeData(path, drugs);
    }

    @Override
    public List<Fruit> getSearchFruitList(String searchContent, List<Fruit> userDrugsList) {
        List<Fruit> drugListSearch = new ArrayList<>();
        for (Fruit drugSearch : userDrugsList) {
            if(drugSearch.getFruitName().toLowerCase().contains(searchContent)) {
                drugListSearch.add(drugSearch);
            }
        }
        return drugListSearch;
    }
}

