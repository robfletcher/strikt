workflow "New workflow" {
  on = "push"
  resolves = ["new-action"]
}

action "Test" {
  uses = "MrRamych/gradle-actions@master"
  args = "build"
}
