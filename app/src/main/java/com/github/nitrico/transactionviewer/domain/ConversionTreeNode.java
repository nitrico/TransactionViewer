package com.github.nitrico.transactionviewer.domain;

import java.util.LinkedList;
import java.util.List;

/**
 * Tree class used to calculate the conversion rate between currencies when it is not straightforward
 */
public class ConversionTreeNode {

    private Rate rate;
    private ConversionTreeNode parent;
    private List<ConversionTreeNode> children = new LinkedList<>();

    public ConversionTreeNode(Rate item, ConversionTreeNode parent) {
        this.rate = item;
        this.parent = parent;
    }

    public void addChild(Rate rate) {
        children.add(new ConversionTreeNode(rate, this));
    }

    public Rate getRate() {
        return rate;
    }

    public ConversionTreeNode getParent() {
        return parent;
    }

    public List<ConversionTreeNode> getChildren() {
        return children;
    }

    public double getConversionRate(String currency) {
        double conversionRate = 1;
        ConversionTreeNode node = find(currency);
        while (node != null && node.getRate() != null) {
            conversionRate *= node.getRate().rate;
            node = node.getParent();
        }
        return conversionRate;
    }

    public ConversionTreeNode find(String from) {
        return find(this, from);
    }

    private ConversionTreeNode find(ConversionTreeNode currentNode, String from) {
        ConversionTreeNode returnNode = null;
        if (currentNode.rate != null && currentNode.rate.from.equals(from)) {
            returnNode = currentNode;
        }
        else if (currentNode.children.size() > 0) {
            int i = 0;
            while (returnNode == null && i < currentNode.children.size()) {
                returnNode = find(currentNode.children.get(i), from);
                i++;
            }
        }
        return returnNode;
    }

    public void print() {
        print(this, " ");
    }

    private void print(ConversionTreeNode node, String appender) {
        if (node.getRate() != null) {
            System.out.println(appender + node.getRate().toString());
        }
        for (ConversionTreeNode n: node.getChildren()) {
            print(n, appender + appender);
        }
    }

}
