package org.trailsframework.examples.conversations.pages;

import java.util.Random;

import org.apache.tapestry5.annotations.InjectPage;

public class Index
{
  @InjectPage
  private Guess guess;

  Object onAction()
  {
    return guess;
  }
}