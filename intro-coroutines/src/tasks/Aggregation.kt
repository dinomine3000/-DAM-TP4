package tasks

import contributors.User

/*
TODO: Write aggregation code.

 In the initial list each user is present several times, once for each
 repository he or she contributed to.
 Merge duplications: each user should be present only once in the resulting list
 with the total value of contributions for all the repositories.
 Users should be sorted in a descending order by their contributions.

 The corresponding test can be found in test/tasks/AggregationKtTest.kt.
 You can use 'Navigate | Test' menu action (note the shortcut) to navigate to the test.
*/
fun List<User>.aggregate(): List<User> {
    val contributionsMap: HashMap<String, User> = HashMap()
    for(user: User in this){
        contributionsMap[user.login] = contributionsMap.getOrDefault(user.login, User(user.login, 0)).add(user.contributions)
    }
    return contributionsMap.values.sortedByDescending { it.contributions };
}

fun User.add(contributions: Int): User{
    return User(this.login, this.contributions + contributions)
}