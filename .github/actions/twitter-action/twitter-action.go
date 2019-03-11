package main

import (
    "io/ioutil"
    "os"
    "log"
    "flag"
    "github.com/coreos/pkg/flagutil"
    "github.com/dghubble/go-twitter/twitter"
    "github.com/dghubble/oauth1"
)

func main() {
    flags := flag.NewFlagSet("user-auth", flag.ExitOnError)
    consumerKey := flags.String("consumer-key", "", "Twitter Consumer Key")
    consumerSecret := flags.String("consumer-secret", "", "Twitter Consumer Secret")
    accessToken := flags.String("access-token", "", "Twitter Access Token")
    accessSecret := flags.String("access-secret", "", "Twitter Access Secret")
    messageFile := flags.String("file", "", "Tweet Message Content File")
    flags.Parse(os.Args[1:])
    flagutil.SetFlagsFromEnv(flags, "TWITTER")

    if *consumerKey == "" || *consumerSecret == "" || *accessToken == "" || *accessSecret == "" {
        log.Fatal("Consumer key/secret and Access token/secret required")
    }

    dat, _ := ioutil.ReadFile(*messageFile)
    tweetMessage := string(dat)

    // Validating message is empty
    if tweetMessage == "" {
        log.Fatal("Tweet content required")
    }

    // Validating message is 280 char
    if len(tweetMessage) > 280 {
        log.Fatal("Tweet must be less than 280 char")
    }

    // Setup auth
    config := oauth1.NewConfig(*consumerKey, *consumerSecret)
    token := oauth1.NewToken(*accessToken, *accessSecret)

    // http.Client will automatically authorize Requests
    httpClient := config.Client(oauth1.NoContext, token)

    // Twitter client
    client := twitter.NewClient(httpClient)

    // Posting tweet
    _, _, err := client.Statuses.Update(tweetMessage, nil)

    // Handling Error
    if err != nil {
        log.Fatal(err)
    } else {
        log.Printf("Status updated with: " + tweetMessage)
    }
}

