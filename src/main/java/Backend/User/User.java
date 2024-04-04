package Backend.User;

import lombok.Data;

import java.util.Objects;

@Data
public class User {


    public enum TypeUser {
        admin,cashier
    }
    public enum Status {
        online,offline
    }

    private int id;
    private String username;
    private String password;
    private TypeUser type;
    private Status status;

    public User(int id, String username, String password, String type ,String status) {
        this.id = id;
        this.username = username;
        this.password = password;
        switch (type){
            case "admin":this.type = TypeUser.admin;break;
            case "cashier":this.type=TypeUser.cashier;break;
        }
        switch (status){
            case "online":this.status = Status.online;break;
            case "offline":this.status=Status.offline;
        }


    }
    public User(int id, String username, String password, String type ) {
        this.id = id;
        this.username = username;
        this.password = password;
        switch (type){
            case "admin":this.type = TypeUser.admin;break;
            case "cashier":this.type=TypeUser.cashier;break;
        }


    }
    public User(String username, String password, String type,String status){
        this.id = -1;
        this.username = username;
        this.password = password;
        switch (type){
            case "admin":this.type = TypeUser.admin;break;
            case "cashier":this.type=TypeUser.cashier;break;
        }
        switch (status){
            case "online":this.status = Status.online;break;
            case "offline":this.status=Status.offline;
        }

    }
    public User(String username, String password, String type){
        this.id = -1;
        this.username = username;
        this.password = password;
        switch (type){
            case "admin":this.type = TypeUser.admin;break;
            case "cashier":this.type=TypeUser.cashier;break;
        }


    }

    @Override
    public String toString() {
        return id + "\t" + username + "\t" + password + "\t" + type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }


}
