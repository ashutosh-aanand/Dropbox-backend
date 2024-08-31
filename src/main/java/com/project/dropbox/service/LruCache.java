package com.project.dropbox.service;

import com.project.dropbox.entity.FileMetadata;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class LruCache {

    private int capacity, currentSize;
    private CacheNode head, tail;
    Map<String, CacheNode> fileLookup;

    @Getter
    @Setter
    class CacheNode {
        String fileId;
        FileMetadata fileMetadata;
        CacheNode next, prev;

        public CacheNode(){
            fileId = null;
            fileMetadata = null;
            next = null;
            prev = null;
        }

        public CacheNode(String fileId, FileMetadata fileMetadata){
            this.fileId = fileId;
            this.fileMetadata = fileMetadata;
            next = null;
            prev = null;
        }
    }

    public LruCache(int capacity) {
        this.capacity = capacity;
        fileLookup = new HashMap<>();
        head = null;
        tail = null;
        currentSize = 0;
    }

    public FileMetadata get(String fileId){
        if(!fileLookup.containsKey(fileId)) return null;
        FileMetadata foundFileMetadata = fileLookup.get(fileId).getFileMetadata();
        update(fileId, foundFileMetadata);
        return foundFileMetadata;
    }

    public void update(String fileId, FileMetadata fileMetadata){
        if(head == null) {
            head = new CacheNode(fileId, fileMetadata);
            tail = head;
            return;
        }
        CacheNode nodeRef = fileLookup.get(fileId);
        if(nodeRef == null){
            // new node to be added
            nodeRef = new CacheNode(fileId, fileMetadata);
            if(currentSize == capacity){
                deleteLastNode();
            } else currentSize++;
        } else {
            if(nodeRef == head) return;
            removeNode(nodeRef);
        }
        addToFront(nodeRef);
    }

    private void deleteLastNode() {
        if(capacity == 1){
            head = tail = null;
            return;
        }
        CacheNode beforeLastNode = tail.prev;
        beforeLastNode.next = null;
        tail = beforeLastNode;
    }

    private void addToFront(CacheNode nodeRef){
        nodeRef.next = head;
        nodeRef.prev = null;
        head.prev = nodeRef;
        head = nodeRef;
    }

    private void removeNode(CacheNode nodeRef){
        CacheNode prevRef = nodeRef.prev;
        prevRef.next = nodeRef.next;
        if(nodeRef.next != null){
            nodeRef.next.prev = prevRef;
        } else {
            // if this is the last node
            tail = prevRef;
        }
    }
}

/*

file1 -> t1

file2 -> t1 + 5

file3 -> t1 + 10

file1 -> t1 + 15

file4 -> t1 + 20


LRU size is 3

file4
file1
file3


{{f1,t1}, {f2,t2}, {f3,t3}}
find file in cache -> O(n)
updating file time O(n)
removing oldest file in cache O(n)

f1->0
f2->1
f3->2
{{t1,f1}

find file in cache -> O(1)
updating file time O(n)
removing oldest file in cache O(log(n))


head -> {f2,t2}, {f1,t1}, {f3,t3} -> null

Case 1: empty linked list
{f1, t1} -> insert it at the front

Case 2: linked list with available space
    check if the file is already in the linked list
    if yes
        -> lookup its reference in the map
        -> remove it from the linked list
        -> update its access time
        -> add it to the front of the list
        -> return the file
    else
        -> just create a new node with current time
        -> add the node to the front of the list

Case 3: when the cache capacity is full
    check if the file is already in the linked list
    if yes
        -> lookup its reference in the map
        -> remove it from the linked list
        -> update its access time
        -> add it to the front of the list
    else
        -> the last node will be the oldest accessed node
        -> delete the node from the linked list
        -> add a new node for the current file being accessed
* */
