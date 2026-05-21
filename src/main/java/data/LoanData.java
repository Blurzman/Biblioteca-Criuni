package data;

import model.Loan;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

/**
 * In-memory storage for Loan records.
 * Uses the loan's auto-incremented ID as the unique key.
 *
 * @author Samuel
 */
public class LoanData implements BaseData<Loan>, Serializable {
    public LoanData(){
        loans = new HashMap<>();
    }

    /**
     * Removes all loans for which {@link model.Loan#isReturned()} is {@code true}.
     * @author Samuel
     */
    public void removeReturned(){
        getAll().removeIf(Loan::isReturned);
    }

    /**
     * @param loan Loan to store.
     * @throws IllegalArgumentException if a loan with the same ID already exists.
     * @author Samuel
     */
    @Override
    public void add(Loan loan){
        if (!loans.containsKey(loan.getId())) {
            loans.put(loan.getId(), loan);
        } else {
            throw new IllegalArgumentException("Error inesperado. Intente de nuevo"); // Shouldn't ever happen
        }
    }

    /**
     * @param loan Loan to remove.
     * @throws IllegalArgumentException if no loan with that ID exists.
     * @author Samuel
     */
    @Override
    public void remove(Loan loan){
        if (loans.containsKey(loan.getId())){
            loans.remove(loan.getId());
        } else {
            throw new IllegalArgumentException("Ese prestamo no existe"); // Shouldn't ever happen
        }
    }

    /**
     * @param id ID of the loan to retrieve.
     * @return The matching Loan.
     * @throws IllegalArgumentException if no loan with that ID exists.
     * @author Samuel
     */
    @Override
    public Loan get(String id){
        if (loans.containsKey(id)){
            return loans.get(id);
        } else {
            throw new IllegalArgumentException("El prestamo con id " + id + " no existe. Intentelo de nuevo");
        }
    }

    public int getNextId() {
        return loans.keySet().stream()
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0) + 1;
    }

    /**
     * @return All stored loans.
     * @author Samuel
     */
    @Override
    public Collection<Loan> getAll(){
        return loans.values();
    }



    private HashMap<String, Loan> loans = null;
}
