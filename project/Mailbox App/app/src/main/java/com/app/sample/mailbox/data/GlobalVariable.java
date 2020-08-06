package com.app.sample.mailbox.data;

import android.app.Application;

import com.app.sample.mailbox.model.Mail;
import com.app.sample.mailbox.model.People;

import java.util.ArrayList;
import java.util.List;

public class GlobalVariable extends Application {
    private List<People> peoples = new ArrayList<>();
    private List<Mail> inbox = new ArrayList<>();
    private List<Mail> outbox = new ArrayList<>();
    private List<Mail> trash = new ArrayList<>();
    private List<Mail> spam = new ArrayList<>();

    public List<People> getPeoples() {
        return peoples;
    }

    public void setPeoples(List<People> peoples) {
        this.peoples = peoples;
    }

    public List<Mail> getInbox() {
        return inbox;
    }

    public void setInbox(List<Mail> inbox) {
        this.inbox = inbox;
    }

    public void trashInbox(int position){
        trash.add(inbox.get(position));
        inbox.remove(position);
    }
    public void addInbox(Mail mail){
        inbox.add(0, mail);
    }
    public void undoTrashInbox(Mail m, int position){
        inbox.add(position, m);
        removeTrash(m);
    }

    public List<Mail> getOutbox() {
        return outbox;
    }
    public void addOutbox(Mail mail){
        outbox.add(0, mail);
    }
    public void setOutbox(List<Mail> outbox) {
        this.outbox = outbox;
    }

    public void trashOutbox(int position){
        trash.add(outbox.get(position));
        outbox.remove(position);
    }
    public void undoTrashOutbox(Mail m, int position){
        outbox.add(position, m);
        removeTrash(m);
    }

    public List<Mail> getTrash() {
        return trash;
    }

    public void setTrash(List<Mail> trash) {
        this.trash = trash;
    }

    public void addTrash(Mail m){
        trash.add(m);
    }
    private void removeTrash(Mail mail){
        if(trash.contains(mail)) {
            trash.remove(mail);
        }
    }
}
