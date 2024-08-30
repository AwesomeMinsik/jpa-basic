package super_class;

import jakarta.persistence.Entity;

@Entity
public class Book  extends Item{
    private String author;
    private int isbn;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }
}
