package br.com.dequebraeconomy.model;


import java.sql.SQLException;

public enum Category {
        INCOME(1),
        EXPENSE(2),
        INVESTMENT(3),
        GOAL(4);

        private  int id;


        private Category(int id){
            this.id = id;

        }

        public static Category fromId(long id){
            for(Category c : Category.values()){
                if(c.getId() == id){
                    return c;
                }
            }
            throw new IllegalArgumentException(String.format("Categoria inválida %d", id));
        }

        public int getId() {
            return id;
        }



    }
