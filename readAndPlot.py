from matplotlib import pyplot as plt

x = []
y = []
edges = []
activities_x = []
activities_y = []

best_x = []
best_y = []

with open("output_edges.txt")as out_file:
    for i in out_file:
        s = i.split()
        edges.append(s)

with open("output_nodes.txt")as out_file:
    for i in out_file:
        s = i.split()
        x.append(float(s[0]))
        y.append(float(s[1]))

with open("output_activities.txt")as out_file:
    for i in out_file:
        s = i.split()
        activities_x.append(float(s[0]))
        activities_y.append(float(s[1]))

with open("output_best_path.txt")as out_file:
    for i in out_file:
        s = i.split()
        best_x.append(float(s[0]))
        best_y.append(float(s[1]))


# plt.ylim(-1, 3)
# plt.xlim(-1, 3)

plt.ylim(35, 36)
plt.xlim(-117, -116)


# print('Plotting edges')
for e in edges:
    plt.plot([float(e[0]), float(e[2])], [float(e[1]), float(e[3])], 'k-')

# print('Plotting nodes')
plt.plot(x, y, 'ro')
# print('Plotting activities')
plt.plot(activities_x, activities_y, 'bs')
# print('Plotting answer')

plt.plot(best_x[1:-1], best_y[1:-1], 'go-', markersize=7, linewidth=2)
plt.plot([best_x[0]], [best_y[0]], 'ko', markersize=7)
plt.plot([best_x[len(best_x)-1]], [best_y[len(best_y)-1]], 'ko', markersize=7)

plt.show()
